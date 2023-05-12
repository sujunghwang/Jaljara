package com.ssafy.jaljara.ui.screen.child

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.theme.JaljaraTheme
import com.ssafy.jaljara.ui.vm.ContentsViewModel

@Composable
fun ContentsDetailView(contentsViewModel: ContentsViewModel = viewModel()) {
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
            if(soundIdx == -1){
                contentsViewModel.getVideoContentByIdx(videoIdx)
            } else{
                contentsViewModel.getSoundContentByIdx(soundIdx)
            }

        var youtubeUrlPath = contentsInfo.youtubeUrl
        youtubeUrlPath = youtubeUrlPath.split("v=")[1].split("&")[0]
        Log.d("youtubeId입니다.", "$youtubeUrlPath")
//        Log.d("youtubeId입니다.", "$youtubeUrlPath")
//        var


        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(20.dp),
        ) {
            Image(
                painter = painterResource(id = R.drawable.astronoutsleep),
                contentDescription = "icon"
            )
            androidx.compose.material3.Text(
                text = "컨텐츠 상세 페이지",
                style = typography.titleLarge,
                fontWeight = FontWeight.Bold,
                fontSize = 30.sp
            )
        }
        Row(modifier = Modifier.padding(10.dp)) {

            Text(text = "카테고리: ", style = typography.bodyMedium, fontSize = 15.sp)

            Text(text = contentsInfo.contentType, style = typography.bodySmall)
        }

        AndroidView(factory = {
            var view = YouTubePlayerView(it)
            val fragment = view.addYouTubePlayerListener(
                object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        super.onReady(youTubePlayer)
                        youTubePlayer.loadVideo("$youtubeUrlPath", 0f)
                    }
                }
            )
            view
        })

        Text(
            text = contentsInfo.title,
            style = typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(30.dp)
        )

        Text(
            text = contentsInfo.description,
            style = typography.bodyMedium,
            fontWeight = FontWeight.Normal,
            modifier = Modifier.padding(10.dp)
        )

    }


}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    JaljaraTheme {
        ContentsDetailView(
//            contentsInfo = ContentsInfo(
//                0L,
//                "SOUND",
//                "소리제목1",
//                "소리설명1",
//                "https://random.imagecdn.app/500/150",
//                "https://youtu.be/2QD3eyGEUg0"
//            )
        )
    }
}