package com.ssafy.jaljara.network

import com.ssafy.jaljara.data.MissionLog
import com.ssafy.jaljara.data.SleepLog
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path

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
}

object ParentApi {
    val retrofitService: ParentApiService by lazy {
        retrofit.create(ParentApiService::class.java)
    }
}