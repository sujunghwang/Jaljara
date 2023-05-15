package com.ssafy.jaljara.data

data class MissionLog(
    val missionLogId: Long,
    val userId: Long,
    val missionDate: String,
    val missionType: String,
    val content: String,
    val url: String?,
    val isSuccess: Boolean
)
