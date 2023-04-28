package com.ssafy.jaljara.ui.screen.child

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.jaljara.R

@Composable
fun ChildMainView(){
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MissionTodayContainer()
        SetSllepTimeContainer()
        RewardStatusContainer()
        ContentContainer()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionTodayContainer(){
    Column() {
        Text(text = "오늘의 미션")
        Card(
            modifier = Modifier
            .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp,
            ),
            shape = RoundedCornerShape(12.dp),
            content = {
                Button(
                    onClick = { },
                    modifier = Modifier
                        .align(Alignment.End)
                ) {
                    Text(text = "재설정")
                }
                Text(text = "이 닦는 사진 찍기",
                    modifier = Modifier
                        .padding(16.dp)
                        .align(Alignment.CenterHorizontally)
                )

            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetSllepTimeContainer() {
    Column() {
        Text(text = "설정된 수면시간")
        Card(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp,
            ),
            shape = RoundedCornerShape(12.dp),
            content = {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SetTimeContainer(painterResource(id = R.drawable.ic_launcher_foreground), "취침시간", "10:00 PM")
                    SetTimeContainer(painterResource(id = R.drawable.ic_launcher_foreground), "기상시간", "08:00 AM")
                }
            }
        )
    }
}

@Composable
fun SetTimeContainer(img : Painter, title : String, setTime : String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(10.dp)
    ) {
        Image(painter = img,
            contentDescription = null,
        )
        Text(text = title)
        Text(text = setTime)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardStatusContainer() {
    var rewardCnt by remember { mutableStateOf(3) }

    Column() {
        Text(text = "보상 획득 현황")
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 10.dp,
            ),
            shape = RoundedCornerShape(12.dp),
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
                        contentDescription = null,
//                        modifier = Modifier.size(60.dp, 60.dp)
                    )
                    Text(text = "연속 $rewardCnt 번만 성공하면 보상을 획득할 수 있어요!",
                        modifier = Modifier
                            .padding(16.dp)
                    )
                }
            }
        )
    }
}


@Composable
fun ContentContainer() {
    Column() {
        Text(text = "컨텐츠 바로가기")
        Column() {
            Row(
                modifier = Modifier
                    .padding(end = 5.dp, bottom = 5.dp)
            ) {
                ContentCardContainer()
                ContentCardContainer()
                ContentCardContainer()
                ContentCardContainer()
            }
            Row() {
                ContentCardContainer()
                ContentCardContainer()
                ContentCardContainer()
                ContentCardContainer()
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentCardContainer() {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .size(100.dp,70.dp)
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_launcher_foreground),
            contentDescription = null
        )
    }
}

@Preview(showBackground = true)
@Composable
fun MissionTodayContainerView() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        MissionTodayContainer()
        SetSllepTimeContainer()
        RewardStatusContainer()
        ContentContainer()
    }
}