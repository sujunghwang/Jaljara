package com.ssafy.jaljara.utils

import android.content.Context
import android.util.Log
import com.google.gson.reflect.TypeToken
import com.ssafy.jaljara.data.UserInfoWithTokens
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitUtil(context: Context) {
    private val BASE_URL = "https://jaljara.movebxeax.me"
    private var instance: Retrofit? = null
    private val context = context

    private val httpClient = OkHttpClient.Builder().apply {
        addInterceptor(Interceptor { chain -> // get token from shared pref
            Log.e("Interceptor", "hello interceptor")

            val accessToken: String = GetCurrentAccessToken(context)

            var request: Request = when {
                // if exists
                accessToken.isNotEmpty() -> {
                    chain.request().newBuilder().addHeader(
                        "Authorization",    // Header 이름
                        "Bearer ${GetCurrentAccessToken(context)}"    // 값
                    ).build()
                }
                // not exists
                else -> chain.request()
            }

            chain.proceed(request)
        })
    }

    private fun GetCurrentAccessToken(context: Context): String {
        val preferenceUtil = PreferenceUtil<UserInfoWithTokens>(context, "user")
        return when {
            preferenceUtil.hasValue("UserInfoWithTokens") -> {
                preferenceUtil.getValue(
                    "UserInfoWithTokens",
                    null,
                    object : TypeToken<UserInfoWithTokens>() {})!!.accessToken
            }
            else -> {
                ""
            }
        }
    }

    @Synchronized
    fun getInstance(): Retrofit {
        instance?.let {
            return it
        } ?: run {
            instance = Retrofit.Builder().apply {
                baseUrl(BASE_URL)
                addConverterFactory(GsonConverterFactory.create())
                client(httpClient.build())
            }.build()
            return instance!!
        }
    }
}