package com.ssafy.jaljara.ui.screen.parent

import android.app.Application
import android.media.AudioAttributes
import android.media.MediaPlayer
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.vm.ParentViewModel
import com.ssafy.jaljara.ui.screen.child.AudioSlider
import java.io.IOException


@Composable
fun ParentMission(viewModel: ParentViewModel){

    val mission = viewModel.todayMissionResponse
    viewModel.getChildIdByIdx()
    var missionClear by remember { mutableStateOf(mission.isClear) }

    val successToast = Toast.makeText(LocalContext.current, "미션 승인을 완료했습니다.", Toast.LENGTH_SHORT)

    Column(
        Modifier
            .fillMaxSize()
            .padding(8.dp),
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
                text = "미션 검사",
                style = MaterialTheme.typography.titleLarge
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    color = MaterialTheme.colorScheme.tertiary,
                    shape = RoundedCornerShape(12.dp)
                ).padding(10.dp),
        ) {
            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = mission.content,
                    color = Color.White,
                    style = MaterialTheme.typography.titleMedium
                )
                Column(
                    verticalArrangement = Arrangement.Center,
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    mission.url?.let {
                        if (mission.missionType == "IMAGE") {
                            AsyncImage(
                                model = mission.url,
                                contentDescription = null,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.8f)
                            )
                        } else {
                            var player: MediaPlayer? = remember {
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
                                    if (playerPrepared) {
                                        AudioSlider(player = player)
                                    }
                                }
                            ) {
                                // AudioSlider가 파괴 될 때
                                // media player 회수
                                onDispose {
                                    player?.release()
                                    player = null
                                    Log.d("onDispose", "media player 파괴")
                                }
                            }
                        }
                        Button(
                            onClick = {
                                viewModel.setMissionClear(viewModel.selectedChildId)
                                missionClear = true
                                successToast.show()
                            },
                            contentPadding = PaddingValues(vertical = 12.dp),
                            colors = ButtonDefaults.buttonColors(
                                disabledContainerColor = Color.LightGray,
                                disabledContentColor = Color.Gray,
                                containerColor = MaterialTheme.colorScheme.secondary
                            ),
                            modifier = Modifier
                                .padding(8.dp)
                                .fillMaxWidth(0.5f),
                            enabled = !missionClear
                        ) {
                            Text(
                                text = if (missionClear) "승인 완료" else "승인",
                                style = MaterialTheme.typography.titleSmall
                            )
                        }
                    } ?: Column(
                        modifier = Modifier.fillMaxWidth().fillMaxHeight(0.8f),
                        verticalArrangement = Arrangement.Center,
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "미션 미완료",
                            color = Color.White,
                            style = MaterialTheme.typography.titleLarge,
                        )
                    }
                }
            }
        }
    }
}

//fun Button

@Composable
@Preview
fun prev() {
    ParentMission(viewModel = ParentViewModel(LocalContext.current as Application))
}