package com.ssafy.jaljara.utils

import android.util.Log
import com.ssafy.jaljara.data.UserType
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

    fun loginWithToken(token : String,
                       providerType: ProviderType,
                       viewModel: LandingViewModel
    ) {
        Log.e("loginWithToken", "loginWithExternalToken s")
        viewModel.loginWithExternalToken(token = token, provider = providerType)
        Log.e("loginWithToken", "loginWithExternalToken e")
    }

    fun signupWithToken(token : String,
                        providerType: ProviderType,
                        viewModel: LandingViewModel,
                        userType: UserType = UserType.PARENTS,
                        parentCode: String?
    ) {
        Log.e("signupWithToken", "singupExternalToken s")
        viewModel.signupWithExternalToken(token = token, provider = providerType, userType = userType, parentCode = parentCode)
        Log.e("signupWithToken", "singupExternalToken e")
    }
}