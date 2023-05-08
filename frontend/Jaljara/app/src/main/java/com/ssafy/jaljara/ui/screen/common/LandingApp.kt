package com.ssafy.jaljara.ui.screen.common

import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.kakao.sdk.common.KakaoSdk
import com.ssafy.jaljara.R

@Composable
fun LandingApp(
    modifier : Modifier = Modifier,
) {
    initialize()
    LandingScreen()
}

@Composable
private fun initialize() {
    initalizeKakaoAuthentication()
    initalizeGoogleAuthentication()
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