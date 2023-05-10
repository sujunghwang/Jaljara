package com.ssafy.jaljara.data

data class ChildInfo(
    val childId: Long=1,
    val pictureUrl: String = "https://mblogthumb-phinf.pstatic.net/MjAyMDA1MTJfMTY4/MDAxNTg5MjE1ODQyMDM4.YNI2hUs08n7dRnc_oLBRDh57Bd4l7bsXMqdv9jOKz5Mg.Z7QfBAm1ysUYhvOtIUFfctiuaWSrl-obPt4obPBaKCEg.JPEG.z12wow/DA3FC70F-5BC4-441C-A09B-E6D199CC9E05-4613-000001D10EB35418_file.jpg?type=w800",
    val childName: String="오종석"
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
    val rewardId: Long,
    val userId: Int,
    val content: String,
    val getTime: String?,
    val usedTime: String?,
)