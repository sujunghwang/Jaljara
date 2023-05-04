package com.ssafy.jaljara.ui.screen.child

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.ssafy.jaljara.data.todayMission
import com.ssafy.jaljara.data.todayMission2
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.ImageCaptureResult
import com.ujizin.camposer.state.rememberCameraState
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.S)
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
        if (mission.missionType=="IMAGE"){
            Box(){
                CameraUI(Modifier)
            }
            ChildMissionButtons("사진찍기")
        } else {
            Box(){
                RecordUI(Modifier)
            }
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
private fun Context.createNewFile(type:String) = File(
//    filesDir, "${System.currentTimeMillis()}.jpg"
    filesDir, "${System.currentTimeMillis()}."+type
).apply {
    createNewFile()
}

@Composable
fun CameraUI(modifier: Modifier) {
    val cameraState = rememberCameraState()
    val context = LocalContext.current

    CameraPreview(
        cameraState = cameraState,
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .fillMaxHeight(0.8f)
    ) {
        var path by rememberSaveable { mutableStateOf("") }
        Column(
            modifier = Modifier
                .fillMaxWidth(0.7f)
                .fillMaxHeight(0.8f),
            verticalArrangement = Arrangement.Bottom,
            horizontalAlignment = Alignment.CenterHorizontally
        ){
            Button(
                onClick = {
                    val file = context.createNewFile("jpg")
                    cameraState.takePicture(file) { result ->
                        // Result는 사진이 성공적으로 저장되었는지 여부를 알려줌
                        if (result is ImageCaptureResult.Success) { // result.savedUri might be useful to you
                            Toast.makeText(context, "사진찍기 성공!", Toast.LENGTH_LONG).show()
                            Log.d("파일명", file.absolutePath)
                            path = file.absolutePath
                        } else{
                            Toast.makeText(context, "사진찍기 실패..", Toast.LENGTH_LONG).show()
                        }
                    }
                }
            ) {  Text("사진찍기") }
        }
//        AsyncImage(
//            model = ImageRequest.Builder(LocalContext.current)
//                .data(path)
//                .crossfade(true)
//                .build(),
////            placeholder = painterResource(R.drawable.rabbit),
//            contentDescription = null,
////            contentScale = ContentScale.Crop,
////            modifier = Modifier.clip(CircleShape)
//        )
    }

}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun RecordUI(modifier: Modifier){
    val context = LocalContext.current
    val recorder = MediaRecorder(context)

    Row() {
        Button(onClick = {
            val file = context.createNewFile("mp4")
            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
            recorder.setOutputFile(FileOutputStream(file).fd)
            recorder.prepare()
            recorder.start()
            Log.d("파일명", file.absolutePath)
        }) {
            Text("녹음하기")
        }
        Button(onClick = {
            recorder.stop()
            recorder.reset()

        }) {
            Text("녹음 종료하기")
        }
        
    }

}



//@Preview
//@Composable
//fun Prev(){
//    ChildMission()
//}