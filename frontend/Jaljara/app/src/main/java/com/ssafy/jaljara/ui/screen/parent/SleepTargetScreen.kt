package com.ssafy.jaljara.ui.screen.parent

import android.os.Build
import android.util.Log
import android.widget.Space
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.vm.ParentViewModel
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

@RequiresApi(Build.VERSION_CODES.O)
@ExperimentalMaterial3Api
@Composable
fun SetTimeScreen(viewModel : ParentViewModel){
    var sliderPosition by remember { mutableStateOf(-180f..360f) }
    var bedTime by remember {
        mutableStateOf(LocalTime.of(21,0))
    }
    var wakeupTime by remember {
        mutableStateOf(LocalTime.of(6,0))
    }
    var sleepTimeInt by remember {
        mutableStateOf(0)
    }

    var tipClosed by remember {
        mutableStateOf(false)
    }

    LaunchedEffect(Unit){

        bedTime = LocalTime.parse(viewModel.childSleepResponse.targetBedTime, DateTimeFormatter.ofPattern("HH:mm"))
        wakeupTime = LocalTime.parse(viewModel.childSleepResponse.targetWakeupTime, DateTimeFormatter.ofPattern("HH:mm"))

        Log.d("베드타임", bedTime.toString())
        Log.d("웨이크업타임", wakeupTime.toString())

        var bedTimeInt = bedTime.hour * 60 + bedTime.minute
        var wakeupTimeInt = wakeupTime.hour * 60 + wakeupTime.minute
        if(bedTimeInt >= 1080)
            bedTimeInt -= 1440
        if(wakeupTimeInt >= 1080)
            wakeupTimeInt -= 1440

        sliderPosition = bedTimeInt.toFloat()..wakeupTimeInt.toFloat()
        sleepTimeInt = wakeupTimeInt - bedTimeInt
    }

    val boxModifier = Modifier.background(
        color = MaterialTheme.colorScheme.tertiary,
        shape = RoundedCornerShape(12.dp)
    )

    val circleModifier = Modifier
        .background(
            color = MaterialTheme.colorScheme.primary,
            shape = CircleShape
        )
        .padding(8.dp)

    LazyColumn(modifier = Modifier.fillMaxHeight()){
        item {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // 페이지 타이틀
                Row(
                    modifier = Modifier.fillParentMaxHeight(0.2f),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = R.drawable.astronoutsleep), contentDescription = "icon")
                    Text(
                        text = "목표 수면 시간",
                        style = MaterialTheme.typography.titleLarge
                    )
                }

                if(!tipClosed){
                    //첫 번째 박스 (미정)
                    Box(
                        modifier = Modifier
                            .background(
                                color = Color(0x403828B7),
                                shape = RoundedCornerShape(12.dp)
                            )
                            .fillMaxWidth()
                            .fillParentMaxHeight(0.2f)
                    ){
                        Text(text = "x", modifier = Modifier.align(Alignment.TopEnd).padding(end = 8.dp).clickable{
                            tipClosed = true
                        })
                        Text(text = "학동기(6~12세) 수면 시간은 최소 10 ~ 11시간\n" +
                                "청소년기(12~18세) 수면 시간은 최소 9 ~ 9.25시간",
                            modifier = Modifier.align(Alignment.Center),
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }

                    Spacer(modifier = Modifier.fillParentMaxHeight(0.02f))
                }


                //수면 시간 설정 박스(range slider)
                Box(
                    modifier = boxModifier
                        .fillMaxWidth()
                        .fillParentMaxHeight(0.16f)
                ){
                    Box(modifier = Modifier
                        .align(Alignment.TopStart)
                        .padding(12.dp)){
                        Text(
                            text = "수면 시간 설정",
                            style = MaterialTheme.typography.titleSmall,
                        )
                    }
                    Column(modifier = Modifier
                        .align(Alignment.BottomCenter)
                        .padding(bottom = 8.dp)) {
                        RangeSlider(
                            modifier = Modifier,
                            steps = 287,
                            values = sliderPosition,
                            colors = SliderDefaults.colors(
                                thumbColor = Color.White,
                                activeTickColor = Color.Blue
                            ),
                            onValueChange = {
                                var start = it.start
                                var end = it.endInclusive

                                if(start >= end)
                                    start = end - 5

                                sliderPosition = start..end

                                var bedTimeInt = sliderPosition.start.roundToInt()
                                var wakeupTimeInt = sliderPosition.endInclusive.roundToInt()

                                sleepTimeInt = wakeupTimeInt - bedTimeInt

                                if(bedTimeInt < 0)
                                    bedTimeInt += 1440
                                if(wakeupTimeInt < 0)
                                    wakeupTimeInt += 1440

                                bedTime = LocalTime.of(bedTimeInt / 60, bedTimeInt % 60)
                                wakeupTime = LocalTime.of(wakeupTimeInt / 60, wakeupTimeInt % 60)
                            },
                            valueRange = -360f..1080f,
                        )
                    }
                }

                //취침 시간 박스, 기상 시간 박스
                Row(
                    modifier = Modifier
                        .fillParentMaxHeight(0.22f)
                        .padding(
                            vertical =
                            12.dp
                        )
                ){
                    Box(
                        modifier = boxModifier
                            .weight(1f)
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.baseline_king_bed_24),
                                contentDescription = "bed",
                                modifier = circleModifier
                            )
                            Text(
                                text = "취침 시간", style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = bedTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                    Spacer(modifier = Modifier.weight(0.1f))
                    Box(
                        modifier = boxModifier.weight(1f)
                    ){
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                            modifier = Modifier.fillMaxSize()
                        ) {

                            Image(
                                painter = painterResource(id = R.drawable.baseline_alarm_24),
                                contentDescription = "alarm",
                                modifier = circleModifier
                            )
                            Text(
                                text = "기상 시간", style = MaterialTheme.typography.titleSmall
                            )
                            Text(
                                text = wakeupTime.format(DateTimeFormatter.ofPattern("HH:mm")),
                                style = MaterialTheme.typography.bodyMedium
                            )
                        }
                    }
                }

                //목표 수면 시간 박스
                Box(
                    modifier = boxModifier
                        .fillMaxWidth()
                        .fillParentMaxHeight(0.1f)
                ){
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(start = 6.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.baseline_check_24),
                            contentDescription = "check",
                            modifier = circleModifier.size(16.dp)
                        )
                        Text(
                            text = "목표 수면 시간",
                            style = MaterialTheme.typography.titleSmall,
                            modifier = Modifier
                                .padding(start = 6.dp)
                                .weight(1f)
                        )

                        val sleepTimeH = sleepTimeInt / 60
                        val sleepTimeM = sleepTimeInt % 60

                        if(sleepTimeH != 0){
                            Text(
                                text = "${sleepTimeH}시간",
                                style = MaterialTheme.typography.titleSmall,
                                textAlign = TextAlign.End
                            )
                        }
                        if(sleepTimeM != 0)
                            Text(
                                text = " %02d분".format(sleepTimeM),
                                style = MaterialTheme.typography.titleSmall,
                                textAlign = TextAlign.End
                            )
                    }
                }

                //설정 버튼
                Button(
                    onClick = {
                        viewModel.setTargetSleepTime(1, bedTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")), wakeupTime.format(DateTimeFormatter.ofPattern("HH:mm:ss")))
                        viewModel.getChildSleepInfo(1)
                    },
                    contentPadding = PaddingValues(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.secondary),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    Text(
                        text = "설정 완료",
                        style = MaterialTheme.typography.titleSmall
                    )
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@ExperimentalMaterial3Api
@Preview(showSystemUi = true)
fun preview_(){
    SetTimeScreen(ParentViewModel())
}