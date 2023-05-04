package com.ssafy.jaljara.data


data class ChildInfo(
    val id : Int,
    val pictureUrl : String
)

data class ChildSleepInfo (
    val childId: Long,
    val currentReward: String,
    val streakCount: Int,
    val targetBedTime: String,
    val targetWakeupTime: String
){
    constructor() : this(0,"",0,"","")
}

data class TodayMission(
    val missionTodayId: Long,
    val userId: Long,
    val missionId: Long,
    val content: String,
    val missionType: String,
    val remainRerollCount: Int,
    val url: String,
    val isClear: Boolean
){
    constructor() : this(0,0,0,"","",0,"",false)
}