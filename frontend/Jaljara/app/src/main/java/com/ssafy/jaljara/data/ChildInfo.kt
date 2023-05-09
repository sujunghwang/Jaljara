package com.ssafy.jaljara.data

data class ChildInfo(
    val userId: Long,
    val name: String,
    val profileImageUrl: String?,
    val userType: String?
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
    val url: String?,
    val isClear: Boolean
){
    constructor() : this(0,0,0,"","",0,"",false)
}

data class UsedCoupon(
    val rewardId: Int,
    val userId: Int,
    val content: String,
    val getTime: String?,
    val usedTime: String?,
)

data class NotUsedCoupon(
    val rewardId: Int,
    val userId: Int,
    val content: String,
    val getTime: String?,
    val usedTime: String?,
)