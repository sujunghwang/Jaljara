package com.ssafy.jaljara.network

import retrofit2.Response

sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val code: Int) : Result<Nothing>()
}

suspend fun <T : Any> safeApiCall(call: suspend () -> Response<T>): Result<T> {
    return try {
        val myResp = call.invoke()

        if (myResp.isSuccessful) {
            Result.Success(myResp.body()!!)
        } else {
            Result.Error(myResp.code())
        }

    } catch (e: Exception) {
        Result.Error(999)
    }
}