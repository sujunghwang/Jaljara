package com.ssafy.jaljara.ui.screen.parent

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.theme.Navy
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterial3Api
@Composable
fun SetTimeScreen(){
    Box(modifier = Modifier.fillMaxSize()) {
        Image(
            painter = painterResource(R.drawable.bg),
            contentDescription = "background",
            contentScale = ContentScale.FillBounds,
            modifier = Modifier.fillMaxSize()
        )

        var sliderPosition by remember { mutableStateOf(-180f..360f) }

        var bedTime by remember { mutableStateOf(1260) }
        var wakeupTime by remember { mutableStateOf(360) }
        var bedTimeH by remember { mutableStateOf(bedTime / 60) }
        var bedTimeM by remember { mutableStateOf(bedTime % 60) }
        var wakeupTimeH by remember { mutableStateOf(wakeupTime / 60) }
        var wakeupTimeM by remember { mutableStateOf(wakeupTime % 60) }
        var sleepTimeH by remember { mutableStateOf(9) }
        var sleepTimeM by remember { mutableStateOf(0) }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Box(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                Column() {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "수면 시간 설정",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier
                                .weight(5f)
                                .padding(start = 6.dp)
                        )
                    }
                    RangeSlider(
                        steps = 143,
                        values = sliderPosition,
                        onValueChange = {
                            var start = it.start
                            var end = it.endInclusive

                            if(start >= end)
                                start = end - 10

                            sliderPosition = start..end

                            bedTime = sliderPosition.start.roundToInt()
                            wakeupTime = sliderPosition.endInclusive.roundToInt()

                            sleepTimeH = (wakeupTime - bedTime) / 60
                            sleepTimeM = (wakeupTime - bedTime) % 60

                            if(bedTime < 0)
                                bedTime += 1440
                            if(wakeupTime < 0)
                                wakeupTime += 1440

                            bedTimeH = bedTime / 60
                            bedTimeM = bedTime % 60
                            wakeupTimeH = wakeupTime / 60
                            wakeupTimeM = wakeupTime % 60
                        },
                        valueRange = -360f..1080f,
                    )
                }
            }
            Row(
                modifier = Modifier.padding(vertical = 24.dp)
            ){
                Box(
                    modifier = Modifier
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                        .weight(1f)
                        .padding(8.dp)
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_king_bed_24),
                            contentDescription = "bed",
                            modifier = Modifier
                                .background(color = Navy, shape = CircleShape)
                                .padding(8.dp)
                        )
                        Text(
                            text = "취침 시간",
                            fontSize = 16.sp
                        )
                        Text(
                            text = "%02d:%02d".format(bedTimeH, bedTimeM),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
                Spacer(modifier = Modifier.size(12.dp))
                Box(
                    modifier = Modifier
                        .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                        .weight(1f)
                        .padding(8.dp)
                ){
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {

                        Image(
                            painter = painterResource(id = R.drawable.baseline_alarm_24),
                            contentDescription = "alarm",
                            modifier = Modifier
                                .background(color = Navy, shape = CircleShape)
                                .padding(8.dp)
                        )
                        Text(
                            text = "기상 시간",
                            fontSize = 16.sp
                        )
                        Text(
                            text = "%02d:%02d".format(wakeupTimeH, wakeupTimeM),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
            Box(
                modifier = Modifier
                    .background(color = Color.White, shape = RoundedCornerShape(16.dp))
                    .fillMaxWidth()
                    .padding(16.dp)
            ){
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.baseline_check_24),
                        contentDescription = "check",
                        modifier = Modifier
                            .size(32.dp)
                            .background(color = Navy, shape = CircleShape)
                            .padding(8.dp)
                    )
                    Text(
                        text = "목표 수면 시간",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .weight(5f)
                            .padding(start = 6.dp)
                    )
                    Text(
                        text = "${sleepTimeH} 시간",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold
                    )
                    if(sleepTimeM != 0)
                        Text(
                            text = "%02d 분".format(sleepTimeM),
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                }
            }
            Button(
                onClick = {

                },
                contentPadding = PaddingValues(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Blue,
                    contentColor = Color.White),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 24.dp)
            ) {
                Text(
                    text = "설정 완료",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@ExperimentalMaterial3Api
@Preview(showSystemUi = true)
fun preview_(){
    SetTimeScreen()
}