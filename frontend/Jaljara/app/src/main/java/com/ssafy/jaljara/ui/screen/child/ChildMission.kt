package com.ssafy.jaljara.ui.screen.child

import android.content.Context
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.vm.ChildViewModel
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.ImageCaptureResult
import com.ujizin.camposer.state.rememberCameraState
import java.io.File
import java.io.FileOutputStream

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ChildMission(childViewModel :ChildViewModel){
    var prevInfo by rememberSaveable { mutableStateOf(false) }
    var path by rememberSaveable { mutableStateOf("") }
    Image(
        painter = painterResource(R.drawable.bg),
        contentDescription = "background",
        contentScale = ContentScale.FillBounds,
        modifier = Modifier.fillMaxSize()
    )
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val mission = childViewModel.todayMissionResponse
        childViewModel.getTodayMission(1)

        Text(text = mission.content, color = Color.White)
        if (mission.missionType=="IMAGE"){
            Box(){
                if(prevInfo){
                    AsyncImage(
                        model = ImageRequest.Builder(LocalContext.current)
                            .data(path)
                            .crossfade(true)
                            .build(),
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .fillMaxHeight(0.8f)
                    )
                } else {
                    //                CameraUI(Modifier)
                    val cameraState = rememberCameraState()
                    val context = LocalContext.current

                    CameraPreview(
                        cameraState = cameraState,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .fillMaxHeight(0.8f)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .fillMaxHeight(0.8f),
                            verticalArrangement = Arrangement.Bottom,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ){
                            OutlinedButton(
                                onClick = {
                                    val file = context.createNewFile("jpg")
                                    cameraState.takePicture(file) { result ->
                                        // Result는 사진이 성공적으로 저장되었는지 여부를 알려줌
                                        if (result is ImageCaptureResult.Success) { // result.savedUri might be useful to you
                                            Toast.makeText(context, "사진찍기 성공!", Toast.LENGTH_LONG).show()
                                            Log.d("파일명", file.absolutePath)
                                            path = file.absolutePath
                                            prevInfo = true
                                        } else{
                                            Toast.makeText(context, "사진찍기 실패..", Toast.LENGTH_LONG).show()
                                        }
                                    }
                                },
                                shape = CircleShape,
                                elevation = ButtonDefaults.buttonElevation(8.dp),
                                colors = ButtonDefaults.buttonColors(Color.White),
                                modifier = Modifier
                                    .size(50.dp, 60.dp)
                                    .padding(bottom = 10.dp)
                            ) {  }
                        }

                    }
                }
            }
            Row() {
                Button(onClick = { prevInfo = false }) {
                    Text("사진찍기", color = Color.Red)
                }
                Spacer(modifier = Modifier.padding(20.dp))
                Button(onClick = { /*TODO*/ }) {
                    Text("완료", color = Color.Green)
                }
            }
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