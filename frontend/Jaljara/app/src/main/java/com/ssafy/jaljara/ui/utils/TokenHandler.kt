package com.ssafy.jaljara.ui.utils

import android.util.Log

enum class ProviderType {
    GOOGLE,
    KAKAO
}

fun checkTokenIsValid(token : String, providerType: ProviderType) {
    Log.e("TokenValidChecker", token);
}