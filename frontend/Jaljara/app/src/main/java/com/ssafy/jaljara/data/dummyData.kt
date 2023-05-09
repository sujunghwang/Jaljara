package com.ssafy.jaljara.data

import com.ssafy.jaljara.ui.screen.Children

object DummyDataProvider {

    val contentList = List<Content>(200){ Content() }
//    val childList = List<ChildInfo>(3){ ChildInfo() }
}

data class DummyChildSleepInfo (
    val childId: Long=1,
    val currentReward: String="이닦는 사진 찍기",
    val streakCount: Int=3,
    val targetBedTime: String="22:00:00",
    val targetWakeupTime: String="08:00:00"
)