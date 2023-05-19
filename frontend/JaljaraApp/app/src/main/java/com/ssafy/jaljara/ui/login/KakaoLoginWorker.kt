package com.ssafy.jaljara.ui.login

import android.app.Activity
import android.util.Log
import android.view.View
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.ClientError
import com.kakao.sdk.common.model.ClientErrorCause
import com.kakao.sdk.user.UserApiClient

class KakaoLoginWorker : Activity() {
    companion object {
        val kakaoLoginBtnListener: View.OnClickListener = object : View.OnClickListener {
            override fun onClick(p0: View?) {
                var context = p0!!.context

                Log.i("jaljara", "onClickKakaoLoginBtn")

                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (error != null) {
                        Log.e(TAG, "토큰 정보 보기 실패", error)
                    }
                    else if (tokenInfo != null) {
                        Log.i(TAG, "토큰 정보 보기 성공" +
                                "\n회원번호: ${tokenInfo.id}" +
                                "\n만료시간: ${tokenInfo.expiresIn} 초")
                    }
                }

                val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
                    if (error != null) {
                        Log.e(TAG, "카카오계정으로 로그인 실패", error)
                    } else if (token != null) {
                        Log.i(TAG, "카카오계정으로 로그인 성공 ${token.accessToken} ${token.refreshToken}")

                        UserApiClient.instance.me { user, error ->
                            if (error != null) {
                                Log.e("jaljara", "사용자 정보 요청 실패", error)
                            }
                            else if (user != null) {
                                Log.i("jaljara", "사용자 정보 요청 성공" +
                                        "\n회원번호: ${user.id}")
                            }
                        }
                    }
                }

                // 카카오톡이 설치되어 있으면 카카오톡으로 로그인, 아니면 카카오계정으로 로그인
                if (UserApiClient.instance.isKakaoTalkLoginAvailable(context)) {
                    UserApiClient.instance.loginWithKakaoTalk(context) { token, error ->
                        if (error != null) {
                            Log.e(TAG, "카카오톡으로 로그인 실패", error)

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
                            Log.i(TAG, "카카오톡으로 로그인 성공 ${token.accessToken} ${token.idToken}")
                        }
                    }
                } else {
                    UserApiClient.instance.loginWithKakaoAccount(context, callback = callback)
                }
            }
        }

        const val TAG = "KakaoSDKSample"

    }
}