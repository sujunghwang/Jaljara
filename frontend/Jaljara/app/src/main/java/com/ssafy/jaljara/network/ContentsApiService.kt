package com.ssafy.jaljara.network

import android.content.Context
import com.ssafy.jaljara.data.ContentsInfo
import com.ssafy.jaljara.data.ContentsListUiState
import com.ssafy.jaljara.data.ContentsUiState
import com.ssafy.jaljara.utils.RetrofitUtil
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ContentsApiService {
    @GET("/api/contents")
    suspend fun getContentsList() : ContentsListUiState

    @GET("/api/contetnts/{contentId}")
    suspend fun getContents(@Path("contentId") contentId : Long) : ContentsUiState

    @GET("/api/contents/type")
    suspend fun getContentsTypeList(@Query(value = "contentType") contentType: String) : List<ContentsInfo>

    companion object {
        var apiService: ContentsApiService? = null
        fun getInstance(context: Context): ContentsApiService {
            if (apiService == null) {
                apiService = RetrofitUtil(context = context).getInstance().create(ContentsApiService::class.java)
            }
            return apiService!!
        }
    }
}