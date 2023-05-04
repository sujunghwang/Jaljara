package com.ssafy.jaljara.network

import com.ssafy.jaljara.data.ChildSleepInfo
import com.ssafy.jaljara.data.TargetSleepInput
import com.ssafy.jaljara.data.NotUsedCoupon
import com.ssafy.jaljara.data.TodayMission
import com.ssafy.jaljara.data.UsedCoupon
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*


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

    @PUT("/api/childinfos/sleep")
    suspend fun setTargetSleepTime(@Body targetSleepInput: TargetSleepInput)
    
    @GET("/api/rewards/used/{childId}")
    suspend fun getUsedCoupon(@Path("childId") childId : Long) : List<UsedCoupon>

    @GET("/api/rewards/{childId}")
    suspend fun getNotUsedCoupon(@Path("childId") childId : Long) : List<NotUsedCoupon>

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