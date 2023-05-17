package com.ssafy.jaljara.data

data class TargetSleepInput(
    val childId: Long,
    val targetBedTime: String,
    val targetWakeupTime: String
)