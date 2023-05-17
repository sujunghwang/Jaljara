package com.ssafy.jaljara.network

import android.content.Context
import com.google.android.gms.location.SleepSegmentEvent
import com.ssafy.jaljara.data.*
import com.ssafy.jaljara.utils.RetrofitUtil
import okhttp3.MultipartBody
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.io.File

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

    @Multipart
    @POST("/api/missions/attachment/{childId}")
    suspend fun setMissionResult(
        @Path("childId") childId : Long,
        @Part file : MultipartBody.Part
    )

    @PUT("/api/rewards/use/{rewardId}")
    suspend fun setCouponUsed(
        @Path("rewardId") rewardId : Long
    )

    @GET("/api/missions/{userId}/reroll")
    suspend fun getMissionReroll(@Path("userId") userId : Long)

    @POST("/api/sleeplogs")
    @JvmSuppressWildcards
    suspend fun sendSleepLog(@Body sleepSegmentEvents: List<SleepSegmentEvent>) : Response<String>

    companion object {
        var apiService: ChildApiService? = null
        fun getInstance(context: Context): ChildApiService {
            if (apiService == null) {
                apiService = RetrofitUtil(context = context).getInstance().create(ChildApiService::class.java)
            }
            return apiService!!
        }
    }
}