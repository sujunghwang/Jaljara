package com.ssafy.jaljara.network

import com.ssafy.jaljara.data.UserLoginRequestDto
import com.ssafy.jaljara.data.UserLoginResponseDto
import com.ssafy.jaljara.data.UserSignupResponseDto
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.POST

private const val BASE_URL =
    "https://jaljara.movebxeax.me"

interface UserApiService {
    @POST("/api/auth/login")
    suspend fun loginWithExternalToken(@Body req : UserLoginRequestDto) : UserLoginResponseDto

    @POST("/api/auth/parent/signup")
    suspend fun signupWithExternalToken(@Body req : UserLoginRequestDto) : UserSignupResponseDto

    companion object{
        var apiService:UserApiService? = null
        fun getInstance() : UserApiService {
            if (apiService == null){
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(UserApiService::class.java)
            }
            return apiService!!
        }
    }
}