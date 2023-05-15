package com.ssafy.jaljara.network

import android.content.Context
import com.ssafy.jaljara.data.UserLoginRequestDto
import com.ssafy.jaljara.data.UserLoginResponseDto
import com.ssafy.jaljara.data.UserSignupResponseDto
import com.ssafy.jaljara.utils.RetrofitUtil
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST
import retrofit2.http.Path

private const val BASE_URL =
    "https://jaljara.movebxeax.me"

interface UserApiService {
    @POST("/api/auth/login")
    suspend fun loginWithExternalToken(@Body req: UserLoginRequestDto): Response<UserLoginResponseDto>

    @POST("/api/auth/parent/signup")
    suspend fun signupWithExternalToken(@Body req: UserLoginRequestDto): Response<UserSignupResponseDto>

    @POST("/api/auth/child/signup/{parentCode}")
    suspend fun sigupChildWithExternalToken(
        @Path("parentCode") parentCode: String,
        @Body req: UserLoginRequestDto
    ): Response<UserSignupResponseDto>

    companion object {
        var apiService: UserApiService? = null
        fun getInstance(context: Context): UserApiService {
            if (apiService == null) {
                apiService = RetrofitUtil(context = context).getInstance().create(UserApiService::class.java)
            }
            return apiService!!
        }
    }
}