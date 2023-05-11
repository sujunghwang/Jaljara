package com.ssafy.jaljara.ui.screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.himanshoe.kalendar.component.day.config.KalendarDayColors
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.ChildInfo
import com.ssafy.jaljara.data.ChildSleepInfo
import com.ssafy.jaljara.data.DummyDataProvider
import com.ssafy.jaljara.data.TodayMission
import com.ssafy.jaljara.ui.component.ErrorScreen
import com.ssafy.jaljara.ui.component.LoadingScreen
import com.ssafy.jaljara.ui.screen.child.*
import com.ssafy.jaljara.ui.screen.parent.JongSeokCalendar
import com.ssafy.jaljara.ui.vm.CalendarViewModel
import com.ssafy.jaljara.ui.vm.ParentViewModel
import com.ssafy.jaljara.utils.UiState
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import java.text.SimpleDateFormat
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ParentMainView(parentViewModel: ParentViewModel,
                   onClickMissionParent: () -> Unit,
                   onClickSetTime: () -> Unit,
                   userId : Long,
){
    val scrollState = rememberScrollState()
//    var childSleepInfo = parentViewModel.childSleepResponse
//    var todayMission = parentViewModel.todayMissionResponse
//    var childList = parentViewModel.childList
    //val state by remember { mutableStateOf(parentViewModel.uiState.value.selectedChildrenIdx)}

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {


        parentViewModel.getChildList(userId)
        val childIdx = parentViewModel.selectedChildIdx

//        parentViewModel.getChildren()
        val childList = parentViewModel.childList
        val childPk = if(childList.isNotEmpty()) childList[childIdx].userId else null

        if(childPk != null){
            parentViewModel.getTodayMission(childPk)
            parentViewModel.getChildSleepInfo(childPk)
        }

        val childSleepInfo = parentViewModel.childSleepResponse
        val todayMission = parentViewModel.todayMissionResponse

        Children(parentViewModel, childList, userId)
            CurrentRewardContainer(R.drawable.reward_2,"현재 보상", childSleepInfo.currentReward)
            CurrentRewardContainer(R.drawable.current_reward_2,"오늘의 미션", todayMission.content, Modifier.clickable{onClickMissionParent()})
            Row(modifier = Modifier.fillMaxWidth()) {
                ChildSetTimeCard(painterResource(id = R.drawable.baseline_alarm_24),"Wake Up",
                    "${childSleepInfo.targetWakeupTime}", Modifier.weight(1f))
                Spacer(modifier = Modifier.weight(0.1f))
                ChildSetTimeCard(img = painterResource(id = R.drawable.baseline_king_bed_24), title = "수면 설정하기",
                    content ="${calTime(childSleepInfo)}" ,
                    modifier = Modifier
                        .weight(1f)
                        .clickable { onClickSetTime() })
//                ChildSetTimeCard(
//                    modifier = Modifier
//                        .weight(1f)
//                        .clickable { onClickSetTime() },
//                    img = painterResource(id = R.drawable.baseline_king_bed_24),
//                    title = "수면 설정하기",
//                    content = "8H")
            }
        }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Children(parentViewModel: ParentViewModel, children: List<ChildInfo>, parentId: Long){

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        content = {
            LazyRow(
                verticalAlignment = Alignment.CenterVertically
            ) {

                itemsIndexed(children){ index, item ->
                    Child(parentViewModel, item, index, parentId)
                }
                item(){
                    Image(painter = painterResource(id = R.drawable.ic_person_add),
                        contentDescription = null,
                        modifier = Modifier
                            .size(50.dp, 50.dp)
                            .clickable {
                                Log.d("아이등록 API 요청", "여기다가 뭘할까")
                            }
                    )
                }
            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Child(parentViewModel: ParentViewModel, childInfo: ChildInfo, idx: Int, parentId:Long) {
    var showDialog by remember { mutableStateOf(false) }
    //var selectedChildId by remember { mutableStateOf(selectedChildId) }

    val state by parentViewModel.uiState.collectAsState()

    // 이미지 비트맵
    val bitmap : MutableState<Bitmap?> = mutableStateOf(null)

    //현재 컨텍스트 가져오기
    //이걸 비트맵으로 받겠다
    //어떤 URL인데
    //
    Glide.with(LocalContext.current)
        .asBitmap()
        .load(childInfo.profileImageUrl)
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
                .padding(start = 2.dp, top = 5.dp, end = 7.dp)
                .clip(CircleShape)
                .border(
                    // selectedChildId와 현재 Child의 childInfo.userId가 같을 경우 border표시
                    width = if (parentViewModel.selectedChildIdx == idx) 5.dp else 0.dp,
                    brush = Brush.verticalGradient(listOf(Color.Red, Color.Blue)),
                    shape = CircleShape
                )
                .pointerInput(Unit) {
                    detectTapGestures(
                        onLongPress = {
                            showDialog = true
                            Log.d("아이등록해제모달 요청", "$showDialog")
                        },
                        onTap = {
                            Log.d("아이 선택", "${childInfo.userId}")
                            parentViewModel.selectedChildIdx = idx
                        }
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
                modifier = Modifier
                    .size(50.dp,50.dp)
            ) // 비트맵이 없다면
        }
        Text(text = childInfo.name, style = MaterialTheme.typography.titleSmall)
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
                            //아이 등록 해제 api call
                            parentViewModel.deleteChild(childInfo.userId)
                            //=>전체 아이 리스트 api call
                            parentViewModel.childList = parentViewModel.getChildList(parentId)
                            parentViewModel.selectedChildIdx = 0
                            showDialog = false
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
                    color = Color.Blue,
                )
            }
        )
    }
}


@OptIn(ExperimentalMaterial3Api::class, ExperimentalGlideComposeApi::class)
@Composable
fun CurrentRewardContainer(img : Int, title:String, content: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
            ) {
                GlideImage(
                    model = img,
                    contentDescription = null,
                )
//                Image(painter = img,
//                    contentDescription = null,
//                    modifier = Modifier.size(110.dp, 110.dp)
//                        .padding(15.dp)
//                )
                Column() {
                    Text(text = title, style = MaterialTheme.typography.titleSmall)
                    Text(text = "$content")
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildSetTimeCard(img : Painter,title:String, content: String, modifier: Modifier = Modifier,
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        content = {
            Column(
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()
            ) {
                Text(text = "$content", style = MaterialTheme.typography.titleSmall)
                Text(text = title)
                Image(painter = img,
                    contentDescription = null,
                    modifier = Modifier
                        .align(Alignment.End)
                        .size(100.dp, 105.dp)
                        .padding(5.dp)
                )
            }
        }
    )
}

@RequiresApi(Build.VERSION_CODES.O)
fun calTime(childSleepInfo: ChildSleepInfo): String {

    if(childSleepInfo.targetBedTime.isBlank()) return "수면설정을 진행해 주세요!"

    var bedTime = LocalTime.parse(childSleepInfo.targetBedTime, DateTimeFormatter.ofPattern("HH:mm"))
    var wakeupTime = LocalTime.parse(childSleepInfo.targetWakeupTime, DateTimeFormatter.ofPattern("HH:mm"))

    Log.d("베드타임", bedTime.toString())
    Log.d("웨이크업타임", wakeupTime.toString())

    var bedTimeInt = bedTime.hour * 60 + bedTime.minute
    var wakeupTimeInt = wakeupTime.hour * 60 + wakeupTime.minute
    if (bedTimeInt > wakeupTimeInt)
        bedTimeInt -= 1440

    var sleepTime = LocalTime.of((wakeupTimeInt - bedTimeInt) / 60, (wakeupTimeInt - bedTimeInt) % 60)

    var text = "${sleepTime.hour}H"

    return text
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview(showBackground = true)
@Composable
fun ParentMainScreenView() {
    ParentMainView(
        parentViewModel = viewModel(),
        onClickMissionParent={},
        onClickSetTime={},
        userId =2,
    )
//    val scrollState = rememberScrollState()
//
//    Column(
//        modifier = Modifier
//            .fillMaxHeight()
//            .padding(20.dp)
//            .verticalScroll(scrollState),
//        verticalArrangement = Arrangement.SpaceBetween
//    ) {
////        Children(childList)
////            CurrentRewardContainer(painterResource(R.drawable.current_reward),"현재 보상", "놀이동산 가기")
////            CurrentRewardContainer(painterResource(id = R.drawable.today_mission),"오늘의 미션", "이닦는 사진 찍기")
//        CurrentRewardContainer(R.drawable.reward_2,"현재 보상", "놀이동산 가기")
//        CurrentRewardContainer(R.drawable.current_reward_2,"오늘의 미션", "이닦는 사진 찍기")
//        Row(modifier = Modifier.fillMaxWidth()) {
//            ChildSetTimeCard(painterResource(id = R.drawable.baseline_alarm_24),"Wake Up", "5:00", Modifier.weight(1f))
//            Spacer(modifier = Modifier.weight(0.1f))
//            ChildSetTimeCard(painterResource(id = R.drawable.baseline_king_bed_24),"수면 설정하기", "8H", Modifier.weight(1f))
//        }
//    }

}