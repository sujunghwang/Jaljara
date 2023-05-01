package com.ssafy.jaljara.ui.screen.child

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.jaljara.ui.screen.parent.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildApp(
    modifier: Modifier = Modifier
) {
    Column(
        Modifier.background(color = Color(171,152,226)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChildTitle("오늘의 미션", modifier.padding(top = 10.dp, bottom = 10.dp))
//        CouponScreen(Modifier.background(Color.Transparent))
        ChildMission()
    }
}
