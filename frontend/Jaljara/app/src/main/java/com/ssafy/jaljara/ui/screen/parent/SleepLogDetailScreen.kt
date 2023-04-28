package com.ssafy.jaljara.ui.screen.parent

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun SleepLogDetailScreen(formatDate : String? = "20230428"){
    Column() {
        Text(text = "아이 수면 디테일 페이지")
        Text(text = "선택 된 날짜 : $formatDate")
    }
}