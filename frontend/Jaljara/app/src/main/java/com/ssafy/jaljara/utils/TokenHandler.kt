package com.ssafy.jaljara.utils

import android.content.Context
import android.util.Log
import com.ssafy.jaljara.ui.vm.LandingViewModel

class TokenHandler private constructor() {
    enum class ProviderType {
        GOOGLE,
        KAKAO
    }


    companion object {
        private var instance : TokenHandler? = null
        fun getInstance(): TokenHandler {
            return instance ?: synchronized(this) {
                instance ?: TokenHandler().also {
                    instance = it
                }
            }
        }
    }

    fun checkTokenIsValid(token : String, providerType: ProviderType, viewModel: LandingViewModel) {
        Log.e("TokenValidChecker", token)
        viewModel.loginWithExternalToken(token = token, provider = providerType)
    }

    fun signupWithToken(token : String, providerType: ProviderType, viewModel: LandingViewModel) {
        viewModel.signupWithExternalToken(token = token, provider = providerType)
    }
}