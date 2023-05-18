package com.ssafy.jaljara.network

import android.content.Context
import com.ssafy.jaljara.data.ChildInfo
import com.ssafy.jaljara.data.MissionLog
import com.ssafy.jaljara.data.ParentCode
import com.ssafy.jaljara.data.SleepLog
import com.ssafy.jaljara.utils.RetrofitUtil
import retrofit2.Response
import retrofit2.http.*

interface ParentApiService {
    @GET("/api/sleeplogs/{childId}/{date}/simple")
    suspend fun  getSleepLogSimple(@Path("childId") childId : Long, @Path("date") date: String): List<Int>

    @GET("/api/sleeplogs/{childId}/{date}")
    suspend fun  getSleepLogDetail(@Path("childId") childId : Long, @Path("date") date: String): SleepLog

    @GET("/api/missions/{userId}/{date}")
    suspend fun  getMissionLog(@Path("userId") userId : Long, @Path("date") date: String): MissionLog

    @GET("/api/childinfos/{parentId}")
    suspend fun getChildList(@Path("parentId") parentId : Long): Response<List<ChildInfo>>

    @DELETE("/api/childinfos/{childId}")
    suspend fun deleteChild(@Path("childId") childId : Long)

    @PUT("/api/missions/{childId}/clear")
    suspend fun setMissionClear(@Path("childId") childId: Long)

    @GET("/api/auth/parent/code")
    suspend fun getParentCode(@Query("parentId") parentId: Long,): ParentCode

    @PUT("/api/childinfos/reward")
    suspend fun setReward(
        @Body body: HashMap<String, Any>
//        @Body childId: Long,
//        @Body reward: String
    )

    companion object {
        var apiService: ParentApiService? = null
        fun getInstance(context: Context): ParentApiService {
            if (apiService == null) {
                apiService = RetrofitUtil(context = context).getInstance().create(ParentApiService::class.java)
            }
            return apiService!!
        }
    }
}