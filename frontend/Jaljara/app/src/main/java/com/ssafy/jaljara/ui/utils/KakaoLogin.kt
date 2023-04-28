package com.ssafy.jaljara.ui.utils

import android.content.Context
import android.util.Log
import androidx.compose.runtime.Composable
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

fun kakaoLoginHelper(context : Context) {
    kakaoLoginWorker(context,
        onTokenIdReceived = { token -> checkTokenIsValid(token, ProviderType.KAKAO) },
        onLoginFailed = { error -> Log.e(TAG, error)}
    )
}

private const val TAG = "KakaoLogin"

private fun kakaoLoginWorker(
    context : Context,
    onTokenIdReceived: (String) -> Unit,
    onLoginFailed: (String) -> Unit
) {
    val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
        if (error != null) {
            onLoginFailed(error.localizedMessage)
        } else if (token != null) {
            onTokenIdReceived(token.idToken!!)
        }
    }

    // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
    if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
        UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
            if (error != null) {
                onLoginFailed(error.localizedMessage)

                // 유저에 의해서 카카오톡으로 로그인이 취소된 경우 카카오계정으로 로그인 생략 (ex 뒤로가기)
                if (error is ClientError && error.reason == ClientErrorCause.Cancelled) {
                    return@loginWithKakaoTalk
                }

                // 카카오톡에 로그인이 안되어있는 경우 카카오계정으로 로그인
                UserApiClient.instance.loginWithKakaoAccount(
                    context,
                    callback = callback
                )
            } else if (token != null) {
                onTokenIdReceived(token.idToken!!)
            }
        }
    } else {
        UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
    }
}