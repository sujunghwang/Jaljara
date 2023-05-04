package com.ssafy.jaljara.utils

import android.content.Context
import android.util.Log
import com.ssafy.jaljara.ui.vm.LandingViewModel

enum class ProviderType {
    GOOGLE,
    KAKAO
}

fun checkTokenIsValid(token : String, providerType: ProviderType, context: Context) {
    Log.e("TokenValidChecker", token);
    LandingViewModel(context).loginWithExternalToken(token = token, provider = providerType)
}

fun signupWithToken(token : String, providerType: ProviderType, context: Context) {
    LandingViewModel(context).signupWithExternalToken(token = token, provider = providerType)
}