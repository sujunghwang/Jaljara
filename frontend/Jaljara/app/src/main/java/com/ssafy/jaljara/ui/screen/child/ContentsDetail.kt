package com.ssafy.jaljara.ui.screen.child

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.findFragment
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
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
                text = "컨텐츠 상세 페이지",
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

                Text(text = contentsTypeToString, style = typography.bodySmall, color = Color.White)
            }

            Log.d("youtubepath 상태확인1", youtubeUrlPath)

            AndroidView(
//                factory = {
//                    var view = YouTubePlayerView(ctx)
////                    path = youtubeUrlPath
//                    val fragment = view.addYouTubePlayerListener(
//                        object : AbstractYouTubePlayerListener() {
//                            override fun onReady(youTubePlayer: YouTubePlayer) {
//                                super.onReady(youTubePlayer)
//                                Log.d("youtubepath 상태확인2", youtubeUrlPath)
//                                youTubePlayer.loadVideo("$youtubeUrlPath", 0f)
//                            }
//                        }
//                    )
//                    view
//                },
//                update = {
//                    var view = getView<YouTubePlayerView>()
//                    if (view == null) {
//                        view = YouTubePlayerView(ctx)
//                    }
//                    val fragment = view.addYouTubePlayerListener(
//                        object : AbstractYouTubePlayerListener() {
//                            override fun onReady(youTubePlayer: YouTubePlayer) {
//                                super.onReady(youTubePlayer)
//                                Log.d("youtubepath 상태확인2", youtubeUrlPath)
//                                youTubePlayer.loadVideo("$youtubeUrlPath", 0f)
//                            }
//                        }
//                    )
//                    view
//                }
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
                modifier = Modifier.padding(30.dp),
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
            Column() {
                LazyRow() {
                    itemsIndexed(contentsSoundList) { index: Int, item: ContentsInfo ->
                        AnotherContentCard(
                            item,
                            index,
                            Modifier.clickable {
                                Log.d("소리 컨텐츠 클릭 - contentIdx", "$index")
                                onClickContent(item)
                                contentsViewModel.selectedVideoIdx = -1
                                contentsViewModel.selectedSoundIdx = index
                            }
                        )
                    }
                }
                LazyRow() {
                    itemsIndexed(contentsVideoList) { index: Int, item: ContentsInfo ->
                        AnotherContentCard(
                            item,
                            index,
                            Modifier.clickable {
                                Log.d("영상 컨텐츠 클릭 - contentIdx", "$index")
                                onClickContent(item)
                                contentsViewModel.selectedVideoIdx = index
                                contentsViewModel.selectedSoundIdx = -1
                            }
                        )
                    }
                }
            }
        }
    }
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AnotherContentCard(content: ContentsInfo, idx:Int, modifier: Modifier= Modifier) {
    // 이미지 비트맵
    val bitmap : MutableState<Bitmap?> = mutableStateOf(null)

    //현재 컨텍스트 가져오기
    //이걸 비트맵으로 받겠다
    //어떤 URL인데
    //
    Glide.with(LocalContext.current)
        .asBitmap()
        .load(content.thumbnailImageUrl)
        .into(object : CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                //이미지 비트맵이 다 로드가 됐을때 들어오는 메소드
                bitmap.value = resource //글라이더 라이브러리를 통해 다운받은 비트맵
            }
            override fun onLoadCleared(placeholder: Drawable?) { }
        })

    androidx.compose.material3.Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        modifier = modifier
            .padding(end = 3.dp, bottom = 5.dp)
    ) {
        // 비트 맵이 있다면
        bitmap.value?.asImageBitmap()?.let { fetchedBitmap ->
            Image(
                bitmap = fetchedBitmap,
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp, 60.dp)
            )
        } ?: Image(
            painter = painterResource(R.drawable.ic_no_content),
            contentScale = ContentScale.Fit,
            contentDescription = null,
        ) // 비트맵이 없다면
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview2() {
    JaljaraTheme {
        ContentsDetailView(
        )
    }
}