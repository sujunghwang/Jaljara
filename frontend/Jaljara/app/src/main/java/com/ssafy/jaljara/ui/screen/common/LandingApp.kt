package com.ssafy.jaljara.ui.screen.common

import android.annotation.SuppressLint
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.gson.reflect.TypeToken
import com.kakao.sdk.common.KakaoSdk
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.UserInfoWithTokens
import com.ssafy.jaljara.data.UserType
import com.ssafy.jaljara.ui.screen.child.ChildApp
import com.ssafy.jaljara.ui.screen.parent.ParentApp
import com.ssafy.jaljara.utils.PreferenceUtil

enum class LandingScreens(val url: String) {
    Landing(url = "/landing"),
    Signup(url = "/signup"),
    Login(url = "/login")
}

@SuppressLint("NewApi")
@Composable
fun LandingApp(
    navController: NavHostController = rememberNavController()
) {
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
            // 로그인 안된 경우
            NavHost(
                navController = navController,
                startDestination = LandingScreens.Landing.url,
            ) {
                composable(route = LandingScreens.Landing.url) {
                    LandingScreen(navigate = { navController.navigate(it) })
                }
                composable(route = LandingScreens.Signup.url) {
                    SignupScreen(navigate = { navController.navigate(it) })
                }
                composable(route = LandingScreens.Login.url) {
                    LoginScreen(navigate = { navController.navigate(it) })
                }
            }

            initalizeKakaoAuthentication()
            initalizeGoogleAuthentication()
        }
    }
}

@SuppressLint("NewApi")
@Composable
private fun initialize() {

    // navigate to target

}

@Composable
private fun initalizeKakaoAuthentication(context: Context = LocalContext.current) {
    KakaoSdk.init(context, stringResource(id = R.string.kakao_sdk_id))
}

@Composable
private fun initalizeGoogleAuthentication() {
    GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
        .requestEmail()
        .build()
}

@Composable
@Preview(showSystemUi = true)
fun LandingScreenPreview() {
    LandingApp()
}