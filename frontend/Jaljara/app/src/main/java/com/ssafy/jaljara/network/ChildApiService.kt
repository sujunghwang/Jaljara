package com.ssafy.jaljara.network

import com.ssafy.jaljara.data.ChildSleepInfo
import com.ssafy.jaljara.data.TodayMission
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path


const val BASE_URL =
    "https://jaljara.movebxeax.me"

//private val retrofit = Retrofit.Builder()
//    .addConverterFactory(ScalarsConverterFactory.create())
//    .baseUrl(BASE_URL)
//    .build()

interface ChildApiService {
    @GET("/api/childinfos/detail/{childId}")
    suspend fun getChildSleepInfo(@Path("childId") childId : Long) : ChildSleepInfo

    @POST("/api/rewards/{childId}")
    suspend fun getReward(@Path("childId") childId : Long)
    
    @GET("/api/missions/{userId}")
    suspend fun getTodayMission(@Path("userId") userId : Long) : TodayMission

    companion object{
        var apiService:ChildApiService? = null
        fun getInstance() : ChildApiService {
            if (apiService == null){
                apiService = Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
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