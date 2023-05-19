package com.ssafy.jaljara.ui.vm

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.ssafy.jaljara.data.LandingUiState
import com.ssafy.jaljara.data.UserInfoWithTokens
import com.ssafy.jaljara.data.UserLoginRequestDto
import com.ssafy.jaljara.data.UserType
import com.ssafy.jaljara.network.Result
import com.ssafy.jaljara.network.UserApiService
import com.ssafy.jaljara.network.safeApiCall
import com.ssafy.jaljara.utils.PreferenceUtil
import com.ssafy.jaljara.utils.TokenHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LandingViewModelState(
    var isLoggedIn: Boolean = false,
    var userType: UserType? = null
)

data class SignupErrorToast(
    var isToastExists: Boolean = false,
    var msg: String = ""
)

class LandingViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val apiService = UserApiService.getInstance(context = application)
    private var preferenceUtil = PreferenceUtil<UserInfoWithTokens>(context, "user")

    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState: StateFlow<LandingUiState> = _uiState.asStateFlow()
    private val _toastState = MutableStateFlow(SignupErrorToast())
    val toastState: StateFlow<SignupErrorToast> = _toastState.asStateFlow()

    fun setUserLoggedIn(_isLoggedIn: Boolean, _userType: UserType) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoggedIn = _isLoggedIn,
                userType = _userType,
                isTokenAvailable = true
            )
        }
    }

    fun loginWithExternalToken(
        token: String,
        provider: TokenHandler.ProviderType,
        isAutoSignup: Boolean = false
    ) {
        Log.e("loginWithExternalToken", "token -> ${token}")
        viewModelScope.launch {
            try {
                val userLoginRequestDto = UserLoginRequestDto(token = token, provider = provider)
                when (val result =
                    safeApiCall { apiService.loginWithExternalToken(userLoginRequestDto) }) {
                    is Result.Success -> {
                        Log.e("loginWithExternalToken", "success")

                        val userInfoWithTokens = UserInfoWithTokens(
                            accessToken = result.data.accessToken,
                            refreshToken = result.data.refreshToken,
                            userInfo = result.data.userInfo
                        )

                        preferenceUtil.setValue("UserInfoWithTokens", userInfoWithTokens)

                        val test = preferenceUtil.getValue(
                            "UserInfoWithTokens",
                            null,
                            object : TypeToken<UserInfoWithTokens>() {})

                        setUserLoggedIn(true, userInfoWithTokens.userInfo.userType)
                        Log.e("LandingViewModel:login", uiState.value.isLoggedIn.toString())
                    }

                    is Result.Error -> {
                        HandleError(result.code)
                    }
                }
            } catch (e: Exception) {
                Log.e("LandingViewModel:login", e.localizedMessage)
            }
        }
    }

    fun signupWithExternalToken(
        token: String,
        provider: TokenHandler.ProviderType,
        userType: UserType = UserType.PARENTS,
        parentCode: String?
    ) {

        viewModelScope.launch {
            try {
                when (userType) {
                    UserType.PARENTS -> {
                        val userLoginRequestDto =
                            UserLoginRequestDto(token = token, provider = provider)
                        when (val result =
                            safeApiCall { apiService.signupWithExternalToken(userLoginRequestDto) }) {
                            is Result.Success -> {
                                loginWithExternalToken(token, provider)
                            }
                            is Result.Error -> {
                                HandleError(result.code)
                            }
                        }

                    }

                    UserType.CHILD -> {
                        val userLoginRequestDto =
                            UserLoginRequestDto(token = token, provider = provider)
                        when (val result =
                            safeApiCall {
                            apiService.sigupChildWithExternalToken(
                                req = userLoginRequestDto,
                                parentCode = parentCode!!
                            )
                        }) {
                            is Result.Success -> {
                                loginWithExternalToken(token, provider)
                            }
                            is Result.Error -> {
                                HandleError(result.code)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                Log.e("LandingViewModel:signup", e.localizedMessage)
            }
        }
    }

    fun HandleError(code : Int) {
        _toastState.update { signupErrorToast ->
            signupErrorToast.copy(
                isToastExists = true,
                msg = when(code) {
                    401 -> "알 수 없는 오류입니다. 잠시 후 다시 시도해주세요."
                    409 -> "이미 존재하는 사용자입니다."
                    else -> "알 수 없는 오류입니다. 잠시 후 다시 시도해주세요."
                }
            )

        }
    }

    fun ClearToast() {
        _toastState.update { signupErrorToast ->
            signupErrorToast.copy(
                isToastExists = false,
                msg = ""
            )
        }
    }
}