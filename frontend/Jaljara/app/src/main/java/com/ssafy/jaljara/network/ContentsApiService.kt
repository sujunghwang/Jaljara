package com.ssafy.jaljara.network

import com.ssafy.jaljara.data.ContentsInfo
import com.ssafy.jaljara.data.ContentsListUiState
import com.ssafy.jaljara.data.ContentsUiState
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL =
    "https://jaljara.movebxeax.me"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ContentsApiService {
    @GET("/api/contents")
    suspend fun getContentsList() : ContentsListUiState

    @GET("/api/contetnts/{contentId}")
    suspend fun getContents(@Path("contentId") contentId : Long) : ContentsUiState

    @GET("/api/contents/type")
    suspend fun getContentsTypeList(@Query(value = "contentType") contentType: String) : List<ContentsInfo>

    companion object{
        var apiService:ContentsApiService? = null
        fun getInstance() : ContentsApiService {
            if (apiService == null) {
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build().create(ContentsApiService::class.java)
            }
            return apiService!!
        }
    }
}

//object ContentsApi {
//    val retrofitService: ContentsApiService by lazy{
//        retrofit.create(ContentsApiService::class.java)
//    }
//}