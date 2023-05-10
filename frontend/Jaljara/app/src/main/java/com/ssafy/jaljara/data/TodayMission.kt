package com.ssafy.jaljara.data

data class TodayMissions(
    val missionTodayId: Int,
    val userId: Int,
    val missionId: Int,
    val missionContent: String,
    val missionType: String,
    val remainRerollCount: Int,
    val url: String?,
    val isClear: Boolean
)

val todayMission = TodayMissions(13,2,4, "어쩌구 사진찍기", "IMAGE",2,null,false)
val todayMission2 = TodayMissions(14,2,5, "저쩌구 녹음하기", "RECORD",2,null,false)
