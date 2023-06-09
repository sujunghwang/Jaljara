package com.ssafy.jaljara.utils

import android.content.Context
import android.util.Log
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient
import com.ssafy.jaljara.data.UserType
import com.ssafy.jaljara.ui.vm.LandingViewModel

fun kakaoSignupHelper(context: Context,
                      viewModel: LandingViewModel,
                      userType: UserType = UserType.PARENTS,
                      parentCode: String?
) {
    kakaoLoginWorker(context,
        onTokenIdReceived = { token -> TokenHandler.getInstance().signupWithToken(token, TokenHandler.ProviderType.KAKAO, viewModel, userType, parentCode) },
        onLoginFailed = { error -> Log.e(TAG, error)}
    )
}

fun kakaoLoginHelper(context : Context, viewModel: LandingViewModel) {
    kakaoLoginWorker(context,
        onTokenIdReceived = { token -> TokenHandler.getInstance().loginWithToken(token, TokenHandler.ProviderType.KAKAO, viewModel) },
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