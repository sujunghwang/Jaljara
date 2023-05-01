package com.ssafy.jaljara.data

object DummyDataProvider {

    val contentList = List<Content>(200){ Content() }
}

data class DummyChildSleepInfo (
    val childId: Long=1,
    val currentReward: String="이닦는 사진 찍기",
    val streakCount: Int=1,
    val targetBedTime: String="22:00:00",
    val targetWakeupTime: String="08:00:00"
)