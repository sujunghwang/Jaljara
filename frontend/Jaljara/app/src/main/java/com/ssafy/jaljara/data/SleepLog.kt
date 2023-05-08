package com.ssafy.jaljara.data

data class SleepLog(
    val userId: Long,
    val date: String,
    val bedTime: String,
    val wakeupTime: String,
    val sleepRate: Double
)
