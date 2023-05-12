package com.ssafy.jaljara.ui.screen.parent

import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.material.MaterialTheme.colors
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.TodayMission
import com.ssafy.jaljara.data.todayMission2
import com.ssafy.jaljara.ui.vm.ParentViewModel
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import java.io.IOException
import com.ssafy.jaljara.ui.screen.child.AudioSlider


@Composable
fun ParentMission(viewModel: ParentViewModel){

    val mission = viewModel.todayMissionResponse
    viewModel.getChildIdByIdx()
    var missionClear by remember { mutableStateOf(mission.isClear) }
//    val mission = todayMission2

    Column(
        Modifier
            .fillMaxHeight()
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        Row(
            modifier = Modifier
                .fillMaxHeight(0.2f)
                .padding(top = 20.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = painterResource(id = R.drawable.astronoutsleep),
                contentDescription = "icon"
            )
            Text(
                text = "미션 확인",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Text(
            text = mission.content,
            color = Color.White,
            style = MaterialTheme.typography.titleSmall
        )
        if (mission.missionType == "IMAGE") {
            Box() {
                mission.url?.let {
                    AsyncImage(
                        model = mission.url,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxWidth(0.7f)
                            .fillMaxHeight(0.8f)
                    )
                } ?: Text(text = "아직 오늘의 미션을 수행하지 않았습니다.`")
            }
            if (missionClear){
                Button(onClick = {  },
                    colors = ButtonDefaults.buttonColors(Color.Gray)) {
                    Text("승인 완료", color = Color.Red)
                }
            } else {
                Button(onClick = {
                    viewModel.setMissionClear(viewModel.selectedChildId)
                    missionClear = true
                }) {
                    Text("승인", color = Color.Red)
                }
            }
        }
        else {
            if(mission.url != null){
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
            } else{
                Text(text = "아직 오늘의 미션을 수행하지 않았습니다.")
            }


            if (missionClear){
                Button(onClick = {  },
                    colors = ButtonDefaults.buttonColors(Color.Gray)) {
                    Text("승인 완료", color = Color.Red)
                }
            } else {
                Button(onClick = {
                    viewModel.setMissionClear(viewModel.selectedChildId)
                    missionClear = true
                }) {
                    Text("승인", color = Color.Red)
                }
            }
        }

    }
}

//fun Button

@Composable
@Preview
fun prev() {
    ParentMission(viewModel = ParentViewModel())
}