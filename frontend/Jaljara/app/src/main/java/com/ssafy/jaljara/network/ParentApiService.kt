package com.ssafy.jaljara.network

import com.ssafy.jaljara.data.ChildInfo
import com.ssafy.jaljara.data.MissionLog
import com.ssafy.jaljara.data.ParentCode
import com.ssafy.jaljara.data.SleepLog
import kotlinx.coroutines.NonDisposableHandle.parent
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

private const val BASE_URL =
    "https://jaljara.movebxeax.me"

private val retrofit = Retrofit.Builder()
    .addConverterFactory(GsonConverterFactory.create())
    .baseUrl(BASE_URL)
    .build()

interface ParentApiService {
    @GET("/api/sleeplogs/{childId}/{date}/simple")
    suspend fun  getSleepLogSimple(@Path("childId") childId : Long, @Path("date") date: String): List<Int>

    @GET("/api/sleeplogs/{childId}/{date}")
    suspend fun  getSleepLogDetail(@Path("childId") childId : Long, @Path("date") date: String): SleepLog

    @GET("/api/missions/{userId}/{date}")
    suspend fun  getMissionLog(@Path("userId") userId : Long, @Path("date") date: String): MissionLog

    @GET("/api/childinfos/{parentId}")
    suspend fun getChildList(@Path("parentId") parentId : Long): List<ChildInfo>

    @DELETE("/api/childinfos/{childId}")
    suspend fun deleteChild(@Path("childId") childId : Long)

    @PUT("/api/missions/{childId}/clear")
    suspend fun setMissionClear(@Path("childId") childId: Long)

    @GET("/api/auth/parent/code")
    suspend fun getParentCode(@Query("parentId") parentId: Long,): ParentCode
}

object ParentApi {
    val retrofitService: ParentApiService by lazy {
        retrofit.create(ParentApiService::class.java)
    }
}