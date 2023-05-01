package com.ssafy.jaljara.network

import com.ssafy.jaljara.data.ChildSleepInfo
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path


private const val BASE_URL =
    "http://k8a802.p.ssafy.io"

//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(ScalarsConverterFactory.create())
//    .baseUrl(BASE_URL)
//    .build()

interface ChildApiService {
    @GET("/api/childinfos/detail/{childId}")
    suspend fun getChildSleepInfo(@Path("childId") childId : Long) : ChildSleepInfo

    companion object{
        var apiService:ChildApiService? = null
        fun getInstance() : ChildApiService {
            if (apiService == null){
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(ScalarsConverterFactory.create())
                    .build().create(ChildApiService::class.java)
            }
            return apiService!!
        }
    }
}

//object ChildApi {
//    val retrofitService : ChildApiService by lazy {
//        retrofit.create(ChildApiService::class.java)
//    }
//}