package com.ssafy.jaljara.ui.screen

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun ParentMain(){
    LazyColumn(content = {
        items(1000) { index ->
            Text(text = "부모 메인 실험 Item: $index")
        }
    })

}