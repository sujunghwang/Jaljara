package com.ssafy.jaljara.ui.screen

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.os.Build
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.foundation.*
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.bumptech.glide.Glide
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.*
import com.ssafy.jaljara.ui.screen.child.*
import com.ssafy.jaljara.ui.vm.ParentViewModel
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
    var showRewardDialog by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(20.dp)
            .verticalScroll(scrollState),
        verticalArrangement = Arrangement.SpaceBetween
    ) {

        parentViewModel.getChildList(userId)
        parentViewModel.getParentCode(userId)
        val parentCode = parentViewModel.parentCode
        val childIdx = parentViewModel.selectedChildIdx

        val childList = parentViewModel.childList
        val childPk = if(childList.isNotEmpty()) childList[childIdx].userId else null

        if(childPk != null){
            parentViewModel.getTodayMission(childPk)
            parentViewModel.getChildSleepInfo(childPk)
        }

        val childSleepInfo = parentViewModel.childSleepResponse
        val todayMission = parentViewModel.todayMissionResponse

        Children(parentViewModel, childList, userId, parentCode)

        CurrentRewardContainer(
            img = R.drawable.reward,
            title = "현재 보상",
            content = childSleepInfo.currentReward,
            modifier = Modifier.pointerInput(Unit) {
            detectTapGestures(
                onTap = {
                    showRewardDialog = true
                }
            )
        })

        TodayMissionContainer(
            img = R.drawable.current_reward,
            title = "오늘의 미션",
            content = todayMission.content,
            modifier = Modifier.clickable{onClickMissionParent()}
        )

        Row(modifier = Modifier.fillMaxWidth()) {
            ChildSetTimeCard(painterResource(id = R.drawable.wake_up),"Wake Up",
                "${childSleepInfo.targetWakeupTime}", Modifier.weight(1f))
            Spacer(modifier = Modifier.weight(0.1f))
            ChildSetTimeCard(img = painterResource(id = R.drawable.bed_time), title = "수면 설정하기",
                content ="${calTime(childSleepInfo)}" ,
                modifier = Modifier
                    .weight(1f)
                    .clickable { onClickSetTime() })
        }
    }

    if (showRewardDialog) {
        var reward by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showRewardDialog = false },
            title = { Text(text = "보상을 등록해주세요", style = MaterialTheme.typography.titleMedium, color = Color.White) },
            text = { TextField(modifier = Modifier
                .fillMaxWidth(), maxLines = 1, value = reward, onValueChange = {reward = it}) },
            confirmButton = {
                Text(
                    text = "확인",
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .clickable {
                            parentViewModel.getChildIdByIdx()
                            Log.d("아이 아이디", "${parentViewModel.selectedChildId}")
                            Log.d("보상 내용", reward)
                            //보상 등록 api call
                            parentViewModel.setReward(parentViewModel.selectedChildId, reward)
                            showRewardDialog = false
                        },
                    color = Color(0x72ff6f).copy(1f),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 20.sp
                )
            },
            dismissButton = {
                Text(
                    text = "취소",
                    modifier = Modifier
                        .padding(start = 8.dp)
                        .clickable {
                            showRewardDialog = false
                        },
                    color = Color(0xff5f72).copy(1f),
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 20.sp
                )
            }
        )
    }
}




@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Children(parentViewModel: ParentViewModel, children: List<ChildInfo>, parentId: Long, parentCode:ParentCode){
    var showDialog by remember { mutableStateOf(false) }
    val clipboardManager = LocalClipboardManager.current
    val context = LocalContext.current

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
                                Log.d("부모 코드", "${parentCode.parentCode}")
                                showDialog = true
                            }
                    )
                }
            }
            if (showDialog) {
                AlertDialog(
                    onDismissRequest = { showDialog = false },
                    title = {
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Text(text = "초대코드 : ${parentCode.parentCode} ")
                            Icon(Icons.Filled.ContentCopy, null, Modifier.clickable {
                                clipboardManager.setText(AnnotatedString(parentCode.parentCode))
                                Toast.makeText(context, "클립보드 복사", Toast.LENGTH_LONG).show()
                            })
                        }
                    },
                    text = { Text(text = "해당 코드를 아이에게 제공해주세요") },
                    confirmButton = {
                        Text(
                            text = "확인",
                            modifier = Modifier
                                .padding(end = 8.dp)
                                .clickable {
                                    showDialog = false
                                },
                            color = Color.Red,
                        )
                    },
                    dismissButton = {
                    }
                )
            }
        }
    )
}

@SuppressLint("UnrememberedMutableState")
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
                        .size(50.dp, 50.dp)
                        .alpha(if (parentViewModel.selectedChildIdx == idx) 1f else 0.3f)
                )
            } ?: Image(painter = painterResource(R.drawable.ic_no_person),
                contentScale = ContentScale.FillBounds,
                contentDescription = null,
                modifier = Modifier
                    .size(50.dp,50.dp)
            ) // 비트맵이 없다면
        }
        Text(text = childInfo.name, style = MaterialTheme.typography.bodySmall, modifier = Modifier.padding(5.dp))
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


@OptIn(ExperimentalMaterial3Api::class)
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
                Image(painter = painterResource(id = img),
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp, 110.dp)
                        .padding(15.dp)
                )
                Column(
                    modifier = modifier.fillMaxWidth(0.8f)
                ) {
                    Text(text = title, style = MaterialTheme.typography.titleMedium, color=Color.White, fontSize = 28.sp)
                    Text(text = if(content == "") "보상을 등록해주세요." else content, style = MaterialTheme.typography.titleSmall, fontSize=16.sp)
                }
                Row(
                    modifier = modifier.fillMaxWidth().padding(12.dp),
                    horizontalArrangement = Arrangement.End
                ){
                    Icon(imageVector = Icons.Filled.Edit, contentDescription = null)
                }
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TodayMissionContainer(img : Int, title:String, content: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier
            .fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.tertiary
        ),
        content = {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(painter = painterResource(id = img),
                    contentDescription = null,
                    modifier = Modifier
                        .size(110.dp, 110.dp)
                        .padding(15.dp)
                )
                Column() {
                    Text(text = title, style = MaterialTheme.typography.titleMedium, color=Color.White, fontSize = 28.sp)
                    Text(text = "$content",style = MaterialTheme.typography.titleSmall, fontSize=16.sp)
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
                Text(
                    text = when (content.isNotEmpty()) {
                        true -> content
                        false -> "-- : --"
                    },
                    color = Color.White,
                    modifier = Modifier.offset(y = -5.dp),
                    fontSize = when (content.startsWith("수면설정")) {
                        true -> 24.sp
                        false -> 40.sp
                    }
                )
                Text(text = title, style = MaterialTheme.typography.titleSmall)
            }
            Image(
                painter = img,
                contentDescription = null,
                modifier = Modifier
                    .align(Alignment.End)
                    .size(100.dp, 100.dp)
                    .padding(top = 10.dp, bottom = 10.dp)
            )
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

    var text = "${sleepTime.hour}H ${sleepTime.minute}M"

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
}