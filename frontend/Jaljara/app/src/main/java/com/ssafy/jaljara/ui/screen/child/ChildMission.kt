package com.ssafy.jaljara.ui.screen.child

import android.content.Context
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.vm.ChildViewModel
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.ImageCaptureResult
import com.ujizin.camposer.state.rememberCameraState
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream

enum class State() {
    BEFORE_RECORDING,
    ON_RECORDING,
    AFTER_RECORDING,
    ON_PLAYING
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ChildMission(childViewModel :ChildViewModel){
    var prevInfo by rememberSaveable { mutableStateOf(false) }
    var path by rememberSaveable { mutableStateOf("") }
    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        verticalArrangement = Arrangement.SpaceAround,
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        val mission = childViewModel.todayMissionResponse
//        childViewModel.getTodayMission(1)
//        val mission = todayMission2

        Text(text = mission.content, color = Color.White)
//        Text(text = mission.missionContent, color = Color.White)
        if (mission.missionType=="IMAGE"){
            Box(){
                Log.d("미리 찍은 사진 여부", mission.url.toString())
                // 미션 수행 기록이 있으면 그 사진으로 대체
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
                Button(onClick = {
                    val file = File(path)
                    val requestBody: RequestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
                    val filePart: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody)
                    //MultipartBody.Part.createFormData("file", file.getName(), RequestBody.create(MediaType.parse("image/*"), file))

                    childViewModel.setMissionResult(1, filePart)
                }) {
                    Text("완료", color = Color.Green)
                }
            }
        } else {
            Box(){
//                RecordUI(Modifier)
                val context = LocalContext.current
                val recorder = MediaRecorder(context)
                val player = MediaPlayer()
                var isRecording by rememberSaveable { mutableStateOf(true) }

                Row() {

//                    Box(
//                        modifier = Modifier.clickable {
//                            val file = context.createNewFile("mp4")
//                            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
//                            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//                            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//                            recorder.setOutputFile(FileOutputStream(file).fd)
//                            recorder.prepare()
//                            recorder.start()
//                            prevInfo = true
//                            Log.d("파일명", file.absolutePath) }
//                    ){
//
//                    }

                    Canvas(
                        modifier = Modifier
                            .size(50.dp)
                            .clickable {  },
                        onDraw = {
                            val size = 50.dp.toPx()
                            val trianglePath = Path().apply {
                                // Moves to top center position
                                moveTo(0f, 0f)
                                // Add line to bottom right corner
                                lineTo(size, size / 2f)
                                // Add line to bottom left corner
                                lineTo(0f, size)
                            }
                            drawPath(
                                color = Color.Green,
                                path = trianglePath
                            )
                        },

                    )
                    Spacer(modifier = Modifier.padding(20.dp))
                    Box(){
                        Button(
                            onClick = {
                                val file = context.createNewFile("mp4")
                                recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
                                recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                                recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                                recorder.setOutputFile(FileOutputStream(file).fd)
                                recorder.prepare()
                                recorder.start()
                                Log.d("파일명", file.absolutePath)
                                path = file.absolutePath
                                isRecording = false
                                prevInfo = true },
                            modifier = Modifier.size(50.dp,50.dp),
                            colors = ButtonDefaults.buttonColors(Color.Red)
                        ) { }
                        Button(
                            onClick = {},
                            modifier = Modifier.size(if(!isRecording){50.dp} else {0.dp}),
                            colors = ButtonDefaults.buttonColors(Color.Gray)
                        ) { }
                    }
                    Spacer(modifier = Modifier.padding(20.dp))
                    Button(
                        onClick = {
                            recorder.stop()
                            recorder.reset()
                        },
                        modifier = Modifier.size(50.dp,50.dp),
                        shape = RectangleShape,
                        colors = ButtonDefaults.buttonColors(Color.Gray)
                    ) {
                        Text("녹음 종료하기")
                    }

                }
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


//@RequiresApi(Build.VERSION_CODES.S)
//@Composable
//fun RecordUI(modifier: Modifier){
//    val context = LocalContext.current
//    val recorder = MediaRecorder(context)
//
//    Row() {
//        Button(onClick = {
//            val file = context.createNewFile("mp4")
//            recorder.setAudioSource(MediaRecorder.AudioSource.MIC)
//            recorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
//            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
//            recorder.setOutputFile(FileOutputStream(file).fd)
//            recorder.prepare()
//            recorder.start()
//            Log.d("파일명", file.absolutePath)
//        }) {
//            Text("녹음하기")
//        }
//        Button(onClick = {
//            recorder.stop()
//            recorder.reset()
//
//        }) {
//            Text("녹음 종료하기")
//        }
//
//    }
//
//}


//@Preview
//@Composable
//fun Prev(){
//    ChildMission()
//}

@Composable
@Preview
fun CircleShape() {

    Canvas(modifier = Modifier.size(50.dp), onDraw = {
        val size = 50.dp.toPx()
        val trianglePath = Path().apply {
            // Moves to top center position
            moveTo(0f, 0f)
            // Add line to bottom right corner
            lineTo(size, size / 2f)
            // Add line to bottom left corner
            lineTo(0f, size)
        }
        drawPath(
            color = Color.Green,
            path = trianglePath
        )
    })
}

