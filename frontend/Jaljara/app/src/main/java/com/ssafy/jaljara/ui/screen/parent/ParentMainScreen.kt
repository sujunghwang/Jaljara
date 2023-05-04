package com.ssafy.jaljara.ui.screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.input.pointer.pointerInput
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
import com.ssafy.jaljara.data.ChildInfo
import com.ssafy.jaljara.data.Content
import com.ssafy.jaljara.data.DummyDataProvider
import com.ssafy.jaljara.ui.screen.child.ContentCard

@Composable
fun ParentMain(){
    LazyColumn(content = {
        items(1000) { index ->
            Text(text = "부모 메인 실험 Item: $index")
        }
    })

}

@Composable
fun ParentMainView(){
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Children(DummyDataProvider.childList)
        CurrentRewardContainer("현재 보상", "놀이동산 가기")
        CurrentRewardContainer("오늘의 미션", "이닦는 사진 찍기")
        Row(modifier = Modifier.fillMaxWidth()) {
            ChildSetTimeCard("Wake Up", "5:00", Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(0.1f))
            ChildSetTimeCard("수면 설정하기", "8H", Modifier.weight(1f))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Children(children: List<ChildInfo>){
    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ),
        shape = RoundedCornerShape(12.dp),
        content = {
            LazyRow(
                verticalAlignment = Alignment.CenterVertically
            ) {
                items(children){ Child(it) }
                item(){
                    Image(painter = painterResource(id = R.drawable.ic_person_add),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp,50.dp)
                    )
                }
            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Child(childInfo: ChildInfo) {
    var showDialog by remember { mutableStateOf(false) }

    // 이미지 비트맵
    val bitmap : MutableState<Bitmap?> = mutableStateOf(null)

    //현재 컨텍스트 가져오기
    //이걸 비트맵으로 받겠다
    //어떤 URL인데
    //
    Glide.with(LocalContext.current)
        .asBitmap()
        .load(childInfo.pictureUrl)
        .into(object : CustomTarget<Bitmap>(){
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                //이미지 비트맵이 다 로드가 됐을때 들어오는 메소드
                bitmap.value = resource //글라이더 라이브러리를 통해 다운받은 비트맵
            }
            override fun onLoadCleared(placeholder: Drawable?) { }
        })
    Column(
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .padding(top= 5.dp,end = 7.dp)
                .clip(CircleShape)
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            showDialog = true
                            Log.d("아이등록해제모달 요청","$showDialog")
                        },
                    )
                }
        ) {
            // 비트 맵이 있다면
            bitmap.value?.asImageBitmap()?.let{fetchedBitmap ->
                Image(bitmap = fetchedBitmap,
                    contentScale = ContentScale.FillBounds,
                    contentDescription = null,
                    modifier = Modifier
                        .size(50.dp,50.dp)
                )
            } ?: Image(painter = painterResource(R.drawable.ic_no_person),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
            ) // 비트맵이 없다면
        }
        Text(text = childInfo.childName)
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text(text = "아이 등록 해제") },
            text = { Text(text = "아이 등록을 해제하시겠습니까?") },
            confirmButton = {
                Text(
                    text = "확인",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            //아이 삭제 로직 구현
                            //아이 등록 해제 api call
                            //=>전체 아이 리스트 api call
                            showDialog=false
                        },
                    color = Color.Red,
                )
            },
            dismissButton = {
                Text(
                    text = "취소",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            showDialog = false
                        },
                )
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CurrentRewardContainer(title:String, content: String) {
    var reward by remember { mutableStateOf(content) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ),
        shape = RoundedCornerShape(12.dp),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
//                        modifier = Modifier.size(60.dp, 60.dp)
                )
                Column() {
                    Text(text = title)
                    Text(text = "$reward", fontSize = 20.sp)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildSetTimeCard(title:String, content: String, modifier: Modifier = Modifier) {

    var time by remember { mutableStateOf(content) }

    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 10.dp,
        ),
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        content = {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "$time", fontSize = 20.sp)
                Text(text = title)
                Image(painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.End)
                )
            }
        }
    )
}


@Preview(showBackground = true)
@Composable
fun ParentMainScreenView() {
    val scrollState = rememberScrollState()
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Children(DummyDataProvider.childList)
        CurrentRewardContainer("현재 보상", "놀이동산 가기")
        CurrentRewardContainer("오늘의 미션", "이닦는 사진 찍기")
        Row(modifier = Modifier.fillMaxWidth()) {
            ChildSetTimeCard("Wake Up", "5:00", Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(0.1f))
            ChildSetTimeCard("수면 설정하기", "8H", Modifier.weight(1f))
        }
    }
}