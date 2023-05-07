package com.ssafy.jaljara.ui.vm

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.reflect.TypeToken
import com.ssafy.jaljara.data.*
import com.ssafy.jaljara.network.UserApiService
import com.ssafy.jaljara.utils.PreferenceUtil
import com.ssafy.jaljara.utils.TokenHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class LandingViewModelState(
    var isLoggedIn : Boolean = false,
    var userType : UserType? = null
)

class LandingViewModel(application: Application) : AndroidViewModel(application) {
    private val context = getApplication<Application>().applicationContext
    private val apiService = UserApiService.getInstance()
    private var preferenceUtil = PreferenceUtil<UserInfoWithTokens>(context, "user")

    private val _uiState = MutableStateFlow(LandingUiState())
    val uiState: StateFlow<LandingUiState> = _uiState.asStateFlow()

    fun setUserLoggedIn(_isLoggedIn : Boolean, _userType : UserType) {
        _uiState.update { currentState ->
            currentState.copy(
                isLoggedIn = _isLoggedIn,
                userType = _userType,
                isTokenAvailable = true
            )
        }
    }

    fun loginWithExternalToken(token : String, provider : TokenHandler.ProviderType){

        viewModelScope.launch {
            try {
                val userLoginRequestDto = UserLoginRequestDto(token = token, provider = provider)
                val response = apiService.loginWithExternalToken(userLoginRequestDto)
                val userInfoWithTokens = UserInfoWithTokens(accessToken = response.accessToken, refreshToken = response.refreshToken, userInfo = response.userInfo)

                preferenceUtil.setValue("UserInfoWithTokens", userInfoWithTokens);

                val test = preferenceUtil.getValue("UserInfoWithTokens", null, object : TypeToken<UserInfoWithTokens>() {})

                setUserLoggedIn(true, userInfoWithTokens.userInfo.userType)
                Log.e("LandingViewModel:login", uiState.value.isLoggedIn.toString());
            } catch (e: Exception) {
                Log.e("LandingViewModel:login", e.localizedMessage);

                /* FIXME!!!!! 바로 여기서 signup하게 할건지? 아니면 따로 action이 필요할까? */
                // signupWithExternalToken(token, provider);
            }
        }
    }

    fun signupWithExternalToken(token : String, provider : TokenHandler.ProviderType){

        viewModelScope.launch {
            try {
                val userLoginRequestDto = UserLoginRequestDto(token = token, provider = provider)
                apiService.signupWithExternalToken(userLoginRequestDto)
            } catch (e: Exception) {
                Log.e("LandingViewModel:signup", e.localizedMessage);
            }
        }
    }
}