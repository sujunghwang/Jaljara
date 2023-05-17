package com.ssafy.jaljara.data.request

data class SleepSegmentEventDto(
    val userId:Long?,
    val startTimeMillis:Long,
    val endTimeMills:Long,
    val segmentDurationMillis:Long,
    val status:Int
)
