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
import com.ssafy.jaljara.data.UserInfo
import com.ssafy.jaljara.data.UserInfoWithTokens
import com.ssafy.jaljara.data.UserLoginRequestDto
import com.ssafy.jaljara.data.UserLoginResponseDto
import com.ssafy.jaljara.network.UserApiService
import com.ssafy.jaljara.utils.PreferenceUtil
import com.ssafy.jaljara.utils.ProviderType
import kotlinx.coroutines.launch

class LandingViewModel(context: Context) : ViewModel() {
    val apiService = UserApiService.getInstance()
    var preferenceUtil : PreferenceUtil<UserInfoWithTokens> = PreferenceUtil(context, "user")

    fun loginWithExternalToken(token : String, provider : ProviderType){

        viewModelScope.launch {
            try {
                val userLoginRequestDto = UserLoginRequestDto(token = token, provider = provider)
                val response = apiService.loginWithExternalToken(userLoginRequestDto)
                val userInfoWithTokens : UserInfoWithTokens = UserInfoWithTokens(accessToken = response.accessToken, refreshToken = response.refreshToken, userInfo = response.userInfo)

                preferenceUtil.setObject("UserInfoWithTokens", userInfoWithTokens);
            } catch (e: Exception) {
                Log.e("LandingViewModel:login", e.localizedMessage);

                /* FIXME!!!!! 바로 여기서 signup하게 할건지? 아니면 따로 action이 필요할까? */
                // signupWithExternalToken(token, provider);
            }
        }
    }

    fun signupWithExternalToken(token : String, provider : ProviderType){

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