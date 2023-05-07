package com.ssafy.jaljara.ui.screen.common

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.reflect.TypeToken
import com.kakao.sdk.common.KakaoSdk
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.UserInfoWithTokens
import com.ssafy.jaljara.data.UserType
import com.ssafy.jaljara.ui.screen.child.ChildApp
import com.ssafy.jaljara.ui.screen.parent.ParentApp
import com.ssafy.jaljara.utils.PreferenceUtil

@Composable
fun LandingApp(
    modifier: Modifier = Modifier,
) {
    initialize()
}

@SuppressLint("NewApi")
@Composable
private fun initialize() {

    // navigate to target
    val preferenceUtil = PreferenceUtil<UserInfoWithTokens>(LocalContext.current, "user")
    when {
        preferenceUtil.hasValue("UserInfoWithTokens") -> {
            // 로그인 된 경우
            val a = preferenceUtil.getValue(
                "UserInfoWithTokens",
                null,
                object : TypeToken<UserInfoWithTokens>() {})
            when (a!!.userInfo.userType) {
                UserType.PARENTS -> ParentApp()
                UserType.CHILD -> ChildApp()
            }
        }
        else -> {
            LandingScreen()
            // 로그인 안된 경우
            initalizeKakaoAuthentication()
            initalizeGoogleAuthentication()
        }
    }
}

@Composable
private fun initalizeKakaoAuthentication(context: Context = LocalContext.current) {
    KakaoSdk.init(context, stringResource(id = R.string.kakao_sdk_id))
}

@Composable
private fun initalizeGoogleAuthentication() {
    val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
}

@Composable
@Preview(showSystemUi = true)
fun LandingScreenPreview() {
    LandingApp()
}