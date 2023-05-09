package com.ssafy.jaljara.ui.screen.child

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
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
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.screen.child.ui.theme.JaljaraTheme

@Composable
fun ContentsDetailView(videoId: String) {
    val ctx = LocalContext.current
    val typography = androidx.compose.material3.MaterialTheme.typography
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Top
    ) {

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

        AndroidView(factory = {
            var view = YouTubePlayerView(it)
            val fragment = view.addYouTubePlayerListener(
                object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        super.onReady(youTubePlayer)
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                }
            )
            view
        })

        Text(
            text = "영상제목1",
            style = typography.bodyMedium,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(30.dp)
        )

        Text(
            text = "영상설명1",
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
        ContentsDetailView("https://youtu.be/ZRtdQ81jPUQ")
    }
}