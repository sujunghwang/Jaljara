package com.ssafy.jaljara.ui.screen.child

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ssafy.jaljara.data.todayMission
import com.ssafy.jaljara.data.todayMission2
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.CamSelector
import com.ujizin.camposer.state.ImageCaptureResult
import com.ujizin.camposer.state.rememberCameraState
import java.io.File

@Composable
fun ChildMission(){
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val mission = todayMission
        Text(text = mission.missionContent)
        Box(
            Modifier.size(180.dp, 350.dp).background(color = Color.Gray)
        ){
            CameraUI(Modifier)
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

// 새 파일을 만들기 위한 함수
private fun Context.createNewFile() = File(
    filesDir, "${System.currentTimeMillis()}.jpg"
).apply { createNewFile() }

@Composable
fun CameraUI(modifier: Modifier) {
    val cameraState = rememberCameraState()
    val context = LocalContext.current
    CameraPreview(
        cameraState = cameraState,
    ) {
        Button(onClick = {
            cameraState.takePicture(context.createNewFile()) { result ->
                // Result는 사진이 성공적으로 저장되었는지 여부를 알려줌
                if (result is ImageCaptureResult.Success) { // result.savedUri might be useful to you
                    Toast.makeText(context, "Success!", Toast.LENGTH_LONG).show()
                } else{
                    Toast.makeText(context, "No!", Toast.LENGTH_LONG).show()
                }
            }
        }) {  Text("Take Picture") }
    }
}
