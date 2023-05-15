package com.ssafy.jaljara.ui.screen.child

import android.annotation.SuppressLint
import android.app.Application
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.ContentsInfo
import com.ssafy.jaljara.ui.vm.ContentsViewModel


@Composable
fun ContentsView(
    contentsViewModel: ContentsViewModel, onClickContents: (ContentsInfo) -> Unit
) {
    val scrollState = rememberScrollState()
    var contentsSoundList = contentsViewModel.contentsSoundListResponse
    var contentsVideoList = contentsViewModel.contentsVideoListResponse
    contentsViewModel.getContentsSoundList()
    contentsViewModel.getContentsVideoList()
    ContentsListView(
        contentsViewModel = contentsViewModel,
        contentsSoundList,
        contentsVideoList,
        onClickContents = onClickContents,
        scrollState = scrollState
    )
}

@Composable
fun ContentsListView(
    contentsViewModel: ContentsViewModel,
    contentsSoundList: List<ContentsInfo>,
    contentsVideoList: List<ContentsInfo>,
    onClickContents: (ContentsInfo) -> Unit,
    scrollState: ScrollState
) {
    val typography = MaterialTheme.typography

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState),
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
                text = "컨텐츠 페이지",
                style = typography.titleLarge,
            )
        }
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Top
        ) {
            Text(
                text = "수면에 도움이 되는 소리",
                color = Color.White,
                style = typography.titleSmall,
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            LazyRow() {
                itemsIndexed(contentsSoundList) { index: Int, item: ContentsInfo ->
                    ContentsItemView(contentsViewModel, item, index, modifier = Modifier.clickable {
                        onClickContents(item)
                        contentsViewModel.selectedVideoIdx = -1
                        contentsViewModel.selectedSoundIdx = index
                    })
                }
            }
            Text(
                text = "수면에 도움이 되는 영상",
                color = Color.White,
                style = typography.titleSmall,
                modifier = Modifier.padding(10.dp),
                fontWeight = FontWeight.SemiBold,
                fontSize = 20.sp
            )
            LazyRow() {
                itemsIndexed(contentsVideoList) { index: Int, item: ContentsInfo ->
                    ContentsItemView(
                        modifier = Modifier.clickable {
                            onClickContents(item)
                            contentsViewModel.selectedVideoIdx = index
                            contentsViewModel.selectedSoundIdx = -1
                        },
                        contentsViewModel = contentsViewModel,
                        contentsInfo = item,
                        index = index,
                    )
                }
            }

        }
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentsItemView(
    contentsViewModel: ContentsViewModel,
    contentsInfo: ContentsInfo,
    index: Int,
    modifier: Modifier = Modifier
) {
    val typography = MaterialTheme.typography
    Log.d("contents info", "${contentsInfo.title}")

    Card(
        modifier = modifier
            .padding(10.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        //fillMaxWidth() 가로로 다 채우기
        //elevation 공중에 뜬거
        //shape 카드의 모양
    ) {
        Column(
            modifier = Modifier.padding(10.dp)

        ) {
            Row(Modifier.padding(10.dp)) {
                var contentsTypeToString = ""
                contentsTypeToString = if (contentsInfo.contentType == "SOUND") {
                    "소리"
                } else {
                    "영상"
                }
                Text(
                    text = "카테고리 : ",
                    style = typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold
                )
                Text(text = contentsTypeToString,
                    style = typography.bodyMedium,
                    color = Color.White,
                    fontWeight = FontWeight.Bold)
            }


            ThumbnailImage(
                thumbnailImageUrl = contentsInfo.thumbnailImageUrl,
                modifier = Modifier
                    .clip(RoundedCornerShape(20.dp))
            )

            Box(
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    horizontalAlignment = Alignment.Start,
                    verticalArrangement = Arrangement.Top
                ) {
                    Text(
                        text = contentsInfo.title,
                        style = typography.bodyMedium,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = contentsInfo.description,
                        style = typography.bodySmall,
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )

                }
            }
        }
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@SuppressLint("UnrememberedMutableState")
@Composable
fun ThumbnailImage(thumbnailImageUrl: String, modifier: Modifier = Modifier) {
    val typography = MaterialTheme.typography
    // 이미지 비트맵
    val bitmap: MutableState<Bitmap?> = mutableStateOf(null)

    val imageModifier = modifier
        .size(200.dp, 120.dp)
        .padding(5.dp)
        .clip(RoundedCornerShape(20.dp))

    //현재 컨텍스트 가져오기
    //이걸 비트맵으로 받겠다
    //어떤 URL인데
    Glide.with(LocalContext.current).asBitmap().load(thumbnailImageUrl)
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                //이미지 비트맵이 다 로드가 됐을때 들어오는 메소드
                bitmap.value = resource //글라이더 라이브러리를 통해 다운받은 비트맵
            }

            override fun onLoadCleared(placeholder: Drawable?) {}
        })

    bitmap.value?.asImageBitmap()?.let { fetchedBitmap ->
        Image(
            bitmap = fetchedBitmap,
            contentScale = ContentScale.Fit,
            contentDescription = null,
            modifier = imageModifier
        )
    } ?: Image(
        painter = painterResource(id = R.drawable.ic_empty_youtube_thumbnail_img),
        contentScale = ContentScale.Fit,
        contentDescription = null,
        modifier = imageModifier
    )
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    val scrollState = rememberScrollState()
    ContentsView(contentsViewModel = ContentsViewModel(LocalContext.current as Application), onClickContents = { it ->
        Log.d("온클릭컨텐츠", "$it 클릭 됨 ㅋㅋ")
    })
}