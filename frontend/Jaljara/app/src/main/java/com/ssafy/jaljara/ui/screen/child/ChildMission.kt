package com.ssafy.jaljara.ui.screen.child

import android.content.Context
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PauseCircleOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.PlayCircleOutline
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.todayMission2
import com.ssafy.jaljara.ui.screen.parent.convertMiliSecToMinSec
import com.ssafy.jaljara.ui.vm.ChildViewModel
import com.ujizin.camposer.CameraPreview
import com.ujizin.camposer.state.CamSelector
import com.ujizin.camposer.state.ImageCaptureResult
import com.ujizin.camposer.state.rememberCamSelector
import com.ujizin.camposer.state.rememberCameraState
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.FileOutputStream
import java.io.IOException


@RequiresApi(Build.VERSION_CODES.S)
@Composable
fun ChildMission(childViewModel :ChildViewModel){
    var prevInfo by rememberSaveable { mutableStateOf(false) }
    var isFirst by rememberSaveable { mutableStateOf(true) }
    var path by rememberSaveable { mutableStateOf("") }
    var isRecording by rememberSaveable { mutableStateOf(false) }
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

        Row(
            modifier = Modifier.fillMaxHeight(0.2f).padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(painter = painterResource(id = R.drawable.astronoutsleep), contentDescription = "icon")
            Text(
                text = "오늘의 미션",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Text(
            text = mission.content,
            color = Color.White,
            style = MaterialTheme.typography.titleSmall)
        if (mission.missionType=="IMAGE"){
            Box(){
                Log.d("isFirst 상태", "$isFirst")
                // 미션 수행 기록이 있으면 그 사진으로 대체

                if(isFirst){
                    mission.url?.let {
                        AsyncImage(
                            model = mission.url,
                            contentDescription = null,
                            modifier = Modifier
                                .fillMaxWidth(0.7f)
                                .fillMaxHeight(0.8f)
                        )
                    }?: Box(modifier = Modifier
                        .fillMaxWidth(0.7f)
                        .fillMaxHeight(0.8f)
                        .background(color = Color.Gray))
                } else {
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
                        Log.d("path 경로",path)
                    } else {
                        val cameraState = rememberCameraState()
                        var zoomRatio by remember { mutableStateOf(cameraState.minZoom) }
                        var camSelector by rememberCamSelector(CamSelector.Back) // Or CamSelector.Front
                        val context = LocalContext.current

                        CameraPreview(
                            cameraState = cameraState,
                            zoomRatio = zoomRatio,
                            onZoomRatioChanged = { zoomRatio = it },
                            isPinchToZoomEnabled = true, // default is true
                            camSelector = camSelector,
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
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(0.7f)
                                    .fillMaxHeight(0.8f)
                                    .padding(end = 10.dp),
                                verticalArrangement = Arrangement.Bottom,
                                horizontalAlignment = Alignment.End
                            ){
                                Box(
                                    modifier = Modifier
                                        .clickable(onClick = {
                                            camSelector = camSelector.inverse // Switch Camera
                                        })
                                        .size(50.dp, 60.dp)
                                        .padding(bottom = 10.dp)
                                        .clip(CircleShape)
                                        .background(Color.Gray),
                                    contentAlignment = Alignment.Center,
                                ) {
                                    Image(
                                        painter = painterResource(R.drawable.baseline_flip_camera_ios_24),
                                        contentDescription = null
                                    )
                                }
                            }
                        }
                    }
                }
            }
            Row() {
                Button(onClick = {
                    prevInfo = false
                    isFirst = false
                }) {
                    Text("사진찍기", color = Color.Red)
                }
                Spacer(modifier = Modifier.padding(20.dp))
                Button(onClick = {
                    val file = File(path)
                    val requestBody: RequestBody = file.asRequestBody("image/jpg".toMediaTypeOrNull())
                    val filePart: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody)

                    childViewModel.setMissionResult(1, filePart)
                }) {
                    Text("완료", color = Color.Green)
                }
            }
        } else {
            Box(){
//                RecordUI(Modifier)
                val context = LocalContext.current
                var recorder:MediaRecorder? by remember { mutableStateOf(null)}

                Row() {

                    Log.d("path 상태", path)

                    Box(
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    isFirst = false
                                    val file = context.createNewFile("mp4")
//                                    recorder = MediaRecorder()
//                                        .apply {
//                                            setAudioSource(MediaRecorder.AudioSource.MIC)
//                                            setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP) // 포멧
//                                            setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB) // 엔코더
//                                            setOutputFile(file)
//                                            prepare()
//                                        }
                                    recorder = MediaRecorder(context)
                                    recorder!!.setAudioSource(MediaRecorder.AudioSource.MIC)
                                    recorder!!.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4)
                                    recorder!!.setAudioEncoder(MediaRecorder.AudioEncoder.AAC)
                                    recorder!!.setOutputFile(FileOutputStream(file).fd)
                                    recorder!!.prepare()
                                    recorder!!.start()
                                    Log.d("파일명", file.absolutePath)
                                    path = file.absolutePath
                                    isRecording = true
                                    prevInfo = true
                                },
                                enabled = !isRecording
                            )
                            .size(50.dp, 50.dp)
                            .background(
                                shape = CircleShape,
                                color = if (isRecording) Color.Gray else Color.Red
                            )
                    )
                    Spacer(modifier = Modifier.padding(20.dp))
                    Box(
                        modifier = Modifier
                            .clickable(
                                onClick = {
                                    Log.d("recorder 상태", "${recorder.toString()}")
                                    recorder?.run {
                                        stop()
                                        Log.d("녹음 종료", "종료")
                                        release()
                                    }
                                    Log.d("recorder 상태", "${recorder.toString()}")
                                    recorder = null
//                                    recorder!!.stop()
//                                    recorder!!.reset()
                                    isRecording = false
                                }
                            )
                            .size(50.dp, 50.dp)
                            .background(
                                shape = RectangleShape,
                                color = if (isRecording) Color.Red else Color.Gray
                            )
                    )
                }
            }

            if(isFirst && mission.url != null){
                var player : MediaPlayer? = remember {
                    MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        try {
                            setDataSource(mission.url)
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        // 백그라운드 스레드에서 미디어 준비
                        prepareAsync()
                    }
                }

                //  compose가 회수될 때 media player 객체 파괴
                DisposableEffect(
                    Column() {
                        var playerPrepared by remember { mutableStateOf(false) }
                        player?.setOnPreparedListener {
                            playerPrepared = true
                        }
                        if(playerPrepared){
                            AudioSlider(player = player)
                        }
                    }
                ){
                    // AudioSlider가 파괴 될 때
                    // media player 회수
                    onDispose{
                        player?.release()
                        player = null
                        Log.d("onDispose", "media player 파괴")
                    }
                }
            }

            if(path != "" && !isRecording){
                var player : MediaPlayer? = remember {
                    MediaPlayer().apply {
                        setAudioAttributes(
                            AudioAttributes.Builder()
                                .setContentType(AudioAttributes.CONTENT_TYPE_MUSIC)
                                .build()
                        )
                        try {
                            setDataSource(path)
                        } catch (e: IllegalArgumentException) {
                            e.printStackTrace()
                        } catch (e: IOException) {
                            e.printStackTrace()
                        }

                        // 백그라운드 스레드에서 미디어 준비
                        prepareAsync()
                    }
                }

                //  compose가 회수될 때 media player 객체 파괴
                DisposableEffect(
                    Column() {
                        var playerPrepared by remember { mutableStateOf(false) }
                        player?.setOnPreparedListener {
                            playerPrepared = true
                        }
                        if(playerPrepared){
                            AudioSlider(player = player)
                        }
                    }
                ){
                    // AudioSlider가 파괴 될 때
                    // media player 회수
                    onDispose{
                        player?.release()
                        player = null
                        Log.d("onDispose", "media player 파괴")
                    }
                }
            }else{

            }

            Button(onClick = {
                val file = File(path)
                val requestBody: RequestBody = file.asRequestBody("audio/mp3".toMediaTypeOrNull())
                val filePart: MultipartBody.Part = MultipartBody.Part.createFormData("file", file.name, requestBody)

                childViewModel.setMissionResult(1, filePart)
            }) {
                Text("완료", color = Color.Green)
            }
        }

    }
}

// 새 파일을 만들기 위한 함수
private fun Context.createNewFile(type:String) = File(
    filesDir, "${System.currentTimeMillis()}."+type
).apply {
    createNewFile()
}

@Composable
fun AudioSlider(modifier: Modifier = Modifier, player : MediaPlayer?) {
    var playing by remember { mutableStateOf(false) }
    var position by remember { mutableStateOf(0F) }

    if (player != null) {

        val displayedPosition = convertMiliSecToMinSec(position)
        val displayedDuration = convertMiliSecToMinSec(player.duration.toFloat())

        // playing 상태가 변경 되면
        LaunchedEffect(playing){
            // 1초에 한 번 MediaPlayer의 position을 변경한다
            while(playing){
                Log.d("LaunchedEffect", "1초에 한 번 ㅋ")
                position = player.currentPosition.toFloat()

                delay(1000)
            }
        }

        // 초 단위가 같으면
        if(position.toInt()/1000 == player.duration/1000){
            position = 0F
            player.seekTo(0)
            player.pause()
            playing = false
        }

        Box(
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = modifier.padding(0.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(modifier = modifier.fillMaxWidth(0.8f)){
                    Text(
                        modifier = Modifier.align(Alignment.BottomStart).padding(top = 4.dp, start = 6.dp),
                        text = "$displayedPosition"
                    )
                    Text(
                        modifier = Modifier.align(Alignment.BottomEnd).padding(top = 4.dp, end = 6.dp),
                        text = "$displayedDuration"
                    )
                    Slider(
                        modifier = Modifier.align(Alignment.TopCenter).padding(bottom = 4.dp),
                        value = position,
                        valueRange = 0F..player.duration.toFloat(),
                        onValueChange = {
                            position = it
                            player.seekTo(it.toInt())
                        }
                    )
                }

                Icon(
                    imageVector = if (!playing) Icons.Filled.PlayArrow else Icons.Filled.Pause,
                    contentDescription = "image",
                    tint = Color.White,
                    modifier = Modifier
                        .size(60.dp)
                        .clickable(onClick = {
                            if (player.isPlaying) {
                                player.pause()
                                playing = false
                            } else {
                                player.start()
                                playing = true
                            }
                        })
                )
            }
        }
    }
}

