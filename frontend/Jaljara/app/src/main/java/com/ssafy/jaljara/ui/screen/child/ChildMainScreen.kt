package com.ssafy.jaljara.ui.screen.child

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.*
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.*
import com.ssafy.jaljara.ui.component.LoadingScreen
import com.ssafy.jaljara.ui.vm.ChildViewModel
import com.ssafy.jaljara.ui.vm.ContentsViewModel
import com.ssafy.jaljara.utils.UiState

@Composable
fun ChildMainView(childViewModel: ChildViewModel,
                  contentsViewModel: ContentsViewModel,
                  onClickMission: () -> Unit,
                  onClickCoupon: () -> Unit,
                  onClickContent: (ContentsInfo)->Unit,
                  userId: Long
){
    val scrollState = rememberScrollState()
    var childSleepInfo = childViewModel.childSleepResponse
    var todayMission = childViewModel.todayMissionResponse
    val soundContents = contentsViewModel.contentsSoundListResponse
    val videoContents = contentsViewModel.contentsVideoListResponse

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        childViewModel.getTodayMission(userId)
        childViewModel.getChildSleepInfo(userId)
        contentsViewModel.getContentsSoundList()
        contentsViewModel.getContentsVideoList()
        MissionTodayContainer(childViewModel,todayMission.content, onClickMission,userId)
        SetSllepTimeContainer(childSleepInfo.targetBedTime,childSleepInfo.targetWakeupTime)
        RewardStatusContainer(childSleepInfo, onClickCoupon)
        ContentContainer(contentsViewModel,soundContents,videoContents, onClickContent)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MissionTodayContainer(childViewModel: ChildViewModel,
                          todayMission: String,
                          onClickMission: () -> Unit = {},
                          userId : Long
){
    val rerollState = childViewModel.rerollUiState

    Column() {
        Text(text = "오늘의 미션", style = MaterialTheme.typography.titleSmall)
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            ),
            onClick = {
                onClickMission()
            },
            content = {
                when {
                    rerollState is UiState.Loading -> LoadingScreen(modifier = Modifier.size(5.dp))
                    rerollState is UiState.Success -> {
                        reloadMissionButton(
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable {
                                    Log.d("missionReload", "미션 재설정 호출")
                                    childViewModel.getMissionReroll(userId)
                                    childViewModel.todayMissionResponse = childViewModel.getTodayMission(userId)
                                    Log.d("missionReload", "미션 재설정 완료")
                                },
                        )
                    }
                    rerollState is UiState.Error -> {
                        if (childViewModel.reroll == 0) {
                            val context = LocalContext.current
                            Toast.makeText(context, "더 이상 재설정 할 수 없어요 ☹", Toast.LENGTH_LONG).show()
                            childViewModel.rerollUiState = UiState.Success("ok")
                        }
                        reloadMissionButton(
                            modifier = Modifier
                                .align(Alignment.End)
                                .clickable {
                                    Log.d("missionReload", "미션 재설정 호출")
                                    childViewModel.getMissionReroll(userId)
                                },
                        )
                    }
                }
                Text(
                    text = "$todayMission",
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .align(Alignment.CenterHorizontally),
                    style = MaterialTheme.typography.titleSmall,
                    fontSize = 24.sp
                )
            }
        )
    }
}

@Composable
fun reloadMissionButton(modifier: Modifier){
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = modifier.padding(top = 8.dp, end = 8.dp)
    ) {
        Text(text = "재설정", style = MaterialTheme.typography.titleSmall, fontSize = 12.sp)
        Image(
            modifier = modifier.height(18.dp).padding(start = 4.dp),
            painter = painterResource(id = R.drawable.ic_reload),
            contentDescription = null
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SetSllepTimeContainer(targetBedTime: String, targetWakeupTime:String) {
    Column() {
        Text(text = "설정된 수면시간", style = MaterialTheme.typography.titleSmall)
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            ),
            content = {
                Row(
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    SetTimeContainer(painterResource(id = R.drawable.bed_time), "취침시간",
                        "${targetBedTime}")
                    SetTimeContainer(painterResource(id = R.drawable.wake_up), "기상시간",
                        "${targetWakeupTime}")
                }
            }
        )
    }
}

@Composable
fun SetTimeContainer(img : Painter, title : String, setTime : String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceAround,
        modifier = Modifier
            .padding(8.dp).width(160.dp)
    ) {
        Image(painter = img,
            contentDescription = null,
            modifier = Modifier
                .size(100.dp, 100.dp)
                .padding(top = 10.dp)
        )
        Text(text = title, style = MaterialTheme.typography.titleSmall, fontSize = 18.sp)
        Text(text = setTime, style = MaterialTheme.typography.bodyLarge, fontSize = 32.sp)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RewardStatusContainer(childSleepInfo: ChildSleepInfo, onClickCoupon: () -> Unit = {}) {
    var remainText by remember { mutableStateOf("") }
    if(childSleepInfo.currentReward == ""){
        remainText = "보상 등록을 부모님께 요청해 보세요!"
    }else{
        if(childSleepInfo.streakCount<7) {
            val remainCnt=7-childSleepInfo.streakCount
            remainText = "${remainCnt}번만 성공하면 보상을 획득할 수 있어요!"
        }
        else {
            remainText="지금 보상을 획득할 수 있어요 ☺"
        }
    }

    Column() {

        Text(text = "보상 획득 현황", style = MaterialTheme.typography.titleSmall)
        Card(
            modifier = Modifier
                .fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.tertiary
            ),
            onClick = {
                onClickCoupon()
            },
            content = {
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(painter = painterResource(id = R.drawable.reward),
                        contentDescription = null,
                        modifier = Modifier
                            .size(90.dp, 90.dp)
                            .padding(start = 10.dp, top= 10.dp, bottom= 10.dp)
                    )
                    Text(text = "$remainText",
                        style = MaterialTheme.typography.titleSmall,
                        fontSize = 16.sp,
                        modifier = Modifier
                            .padding(start = 10.dp, end = 10.dp)
                    )
                }
            }
        )
    }
}


@Composable
fun ContentContainer(contentsViewModel: ContentsViewModel, soundContents: List<ContentsInfo>, videoContents: List<ContentsInfo>,onClickContent: (ContentsInfo) -> Unit = {} ) {
    Column() {
        Text(text = "꿈나라 탐험하기", style = MaterialTheme.typography.titleSmall)
        Column() {
            LazyRow() {
                itemsIndexed(soundContents) { index: Int, item: ContentsInfo ->
                    ContentCard(
                        item,
                        index,
                        Modifier.clickable {
                            Log.d("소리 컨텐츠 클릭 - contentIdx","$index")
                            onClickContent(item)
                            contentsViewModel.selectedVideoIdx = -1
                            contentsViewModel.selectedSoundIdx = index
                        }
                    )
                }
            }
            LazyRow() {
                itemsIndexed(videoContents) { index: Int, item: ContentsInfo ->
                    ContentCard(
                        item,
                        index,
                        Modifier.clickable {
                            Log.d("영상 컨텐츠 클릭 - contentIdx","$index")
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

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ContentCard(content: ContentsInfo, idx:Int, modifier: Modifier= Modifier) {
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

    Card(
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        modifier = modifier
            .padding(end = 3.dp, bottom = 5.dp)
    ) {
        // 비트 맵이 있다면
        bitmap.value?.asImageBitmap()?.let{fetchedBitmap ->
            Image(bitmap = fetchedBitmap,
                contentScale = ContentScale.Fit,
                contentDescription = null,
                modifier = Modifier
                    .size(100.dp,60.dp)
            )
        } ?: Image(painter = painterResource(R.drawable.ic_no_content),
            contentScale = ContentScale.Fit,
            contentDescription = null,
        ) // 비트맵이 없다면
    }
}

@Preview(showBackground = true)
@Composable
fun ChildMainScreenView() {
    ChildMainView(
        childViewModel = viewModel(),
        contentsViewModel = viewModel(),
        onClickCoupon = {},
        onClickMission = {},
        onClickContent = {},
        userId = 1
    )
}