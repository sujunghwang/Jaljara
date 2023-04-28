package com.ssafy.jaljara.ui.screen.child

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ssafy.jaljara.data.todayMission
import com.ssafy.jaljara.data.todayMission2

@Composable
fun ChildMission(){
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val mission = todayMission2
        Text(text = mission.missionContent)
        Box(
            Modifier.size(180.dp, 350.dp).background(color = Color.Gray)
        ){

        }
        if (mission.missionType=="IMAGE"){
            ChildMissionButtons("사진찍기")
        } else {
            ChildMissionButtons("녹음하기")
        }

    }
}

@Composable
fun ChildMissionButtons(text: String){
    Row() {
        Button(onClick = { /*TODO*/ }) {
            Text(text, color = Color.Red)
        }
        Spacer(modifier = Modifier.padding(20.dp))
        Button(onClick = { /*TODO*/ }) {
            Text("완료", color = Color.Green)
        }
    }
}