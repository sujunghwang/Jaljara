package com.ssafy.jaljara.ui.screen.child

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.CenterHorizontally
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.ContentsInfo
import com.ssafy.jaljara.ui.theme.JaljaraTheme
import com.ssafy.jaljara.ui.vm.ContentsViewModel

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun ContentsDetailView(
    contentsViewModel: ContentsViewModel = viewModel(),
    onClickContent: (ContentsInfo) -> Unit = {}
) {
    val scrollState = rememberScrollState()
    var contentsSoundList = contentsViewModel.contentsSoundListResponse
    var contentsVideoList = contentsViewModel.contentsVideoListResponse
    contentsViewModel.getContentsSoundList()
    contentsViewModel.getContentsVideoList()

    val ctx = LocalContext.current
    val typography = androidx.compose.material3.MaterialTheme.typography
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {
        val soundIdx = contentsViewModel.selectedSoundIdx
        val videoIdx = contentsViewModel.selectedVideoIdx

        val contentsInfo =
            if (soundIdx == -1) {
                contentsViewModel.getVideoContentByIdx(videoIdx)
            } else {
                contentsViewModel.getSoundContentByIdx(soundIdx)
            }

        var youtubeUrlPath = contentsInfo.youtubeUrl
        youtubeUrlPath = youtubeUrlPath.split("v=")[1].split("&")[0]
        Log.d("youtubeId입니다.", "$youtubeUrlPath")
//        var contentsTypeToString = ""
        var contentsTypeToString = if (contentsInfo.contentType == "SOUND") {
            "소리"
        } else {
            "영상"
        }
        Log.d("$contentsTypeToString", "$contentsTypeToString")

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.astronoutsleep),
                contentDescription = "icon"
            )
            androidx.compose.material3.Text(
                text = "꿈나라 탐험",
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }

        androidx.compose.material3.Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            ),
        ) {
            Row(modifier = Modifier.padding(10.dp)) {

                Text(
                    text = "카테고리 : ",
                    style = typography.bodyMedium,
//                fontSize = 15.sp,
                    color = Color.White,
                )

                Text(
                    text = contentsTypeToString,
                    style = typography.bodyMedium,
                    color = Color.White
                )
            }

            Log.d("youtubepath 상태확인1", youtubeUrlPath)

            AndroidView(

                factory = { ctx ->
                    var view = YouTubePlayerView(ctx)
                    val fragment = view.addYouTubePlayerListener(
                        object : AbstractYouTubePlayerListener() {
                            override fun onReady(youTubePlayer: YouTubePlayer) {
                                super.onReady(youTubePlayer)
                                Log.d("youtubepath 상태확인2", youtubeUrlPath)
                                youTubePlayer.loadVideo("$youtubeUrlPath", 0f)
                            }
                        }
                    )
                    view
                },
                update = { view ->
                    view as? YouTubePlayerView ?: YouTubePlayerView(ctx).apply {
                        val fragment = addYouTubePlayerListener(
                            object : AbstractYouTubePlayerListener() {
                                override fun onReady(youTubePlayer: YouTubePlayer) {
                                    super.onReady(youTubePlayer)
                                    Log.d("youtubepath 상태확인2", youtubeUrlPath)
                                    youTubePlayer.loadVideo("$youtubeUrlPath", 0f)
                                }
                            }
                        )
                    }
                }
            )

            Text(
                text = contentsInfo.title,
                style = typography.bodyMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                modifier = Modifier.padding(10.dp),
            )

            Text(
                text = contentsInfo.description,
                style = typography.bodyMedium,
                fontWeight = FontWeight.Normal,
                color = Color.White,
                modifier = Modifier.padding(10.dp)
            )
        }
        androidx.compose.material3.Card(
            modifier = Modifier
                .padding(10.dp)
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            ),
        ) {
            Column(verticalArrangement = Arrangement.Center) {
                Text(
                    text = "소리 집중 ASMR",
                    modifier = Modifier
                        .padding(10.dp),
//                        .align(CenterHorizontally)
                    style = MaterialTheme.typography.bodyLarge,
                    fontSize = 15.sp,
                    color = Color.White,
                )
                Text(
                    text = "소리에 집중하면 잠이 더 잘 온다는 연구가 있어요.\n" +
                            "꼭 영상을 보지 않아도 괜찮아요.\n" +
                            "편안하게 눈을 감고 소리를 들어보세요!",
                    modifier = Modifier
                        .padding(10.dp)
                        .align(CenterHorizontally),
                    style = MaterialTheme.typography.bodyMedium,
                    fontSize = 15.sp,
                    color = Color.White,
                )

            }
        }
    }
}

//@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    JaljaraTheme {
        ContentsDetailView(
        )
    }
}
