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
