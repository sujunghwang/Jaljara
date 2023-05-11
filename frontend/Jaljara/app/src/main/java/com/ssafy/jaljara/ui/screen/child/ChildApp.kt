package com.ssafy.jaljara.ui.screen.child

import android.annotation.SuppressLint
import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.twotone.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.screen.StarLink
import com.ssafy.jaljara.ui.theme.DarkNavy
import com.ssafy.jaljara.ui.vm.ChildViewModel
import java.time.LocalDateTime
import java.time.ZoneId
import java.util.*


/**
 * 자녀 앱의 시작점입니다.
 * composable routing 및
 * 공통 bar가 사용됩니다.
 * */

enum class ChildScreen(@StringRes val title: Int, val url: String) {
    Start(title = R.string.main, "/main"),
    StarLink(title = R.string.star_link, "/starlink"),
    Contents(title = R.string.contents, "/contents"),
    Coupon(title = R.string.coupon, "/coupons"),
    Mission(title = R.string.mission, "/mission")
}


data class NavigationInfo(val route: ChildScreen, val icon: ImageVector)
@Composable
fun ChildNavigationBar(
    /** 원하는 아이콘을 찾아서 넣으세요 **/
    items : List<NavigationInfo> = listOf(
        NavigationInfo(ChildScreen.Start, Icons.TwoTone.Home),
        NavigationInfo(ChildScreen.StarLink, Icons.TwoTone.Insights),
        NavigationInfo(ChildScreen.Contents, Icons.TwoTone.Explore),
        NavigationInfo(ChildScreen.Coupon, Icons.TwoTone.Redeem),
        NavigationInfo(ChildScreen.Mission, Icons.TwoTone.Rule)
    ),
    navController : NavController,
    selectedItem : Int,
    onChangeNavIdx: (Int) -> Unit = {},
) {
    NavigationBar(
        modifier = Modifier.clip(shape = RoundedCornerShape(
            topStart = CornerSize(50),
            topEnd = CornerSize(50),
            bottomEnd = CornerSize(0),
            bottomStart = CornerSize(0)
        )),
        containerColor = DarkNavy
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.route.name) },
                label = { Text(stringResource(id = item.route.title), style = MaterialTheme.typography.titleSmall) },
                selected = selectedItem == index,
                onClick = {
                    navController.navigate(item.route.url)
                    onChangeNavIdx(index)
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildApp(
    viewModel: ChildViewModel = viewModel(),
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // 하단 네비게이션 선택 애니메이션 용
    var navBarSelectedItem by rememberSaveable { mutableStateOf(0) }

    val sleepInfo = viewModel.childSleepResponse
    viewModel.getChildSleepInfo(1)

    setAlarm(LocalContext.current, sleepInfo.targetBedTime)

    Scaffold(
        bottomBar = {
            ChildNavigationBar(
                navController = navController,
                selectedItem = navBarSelectedItem,
                onChangeNavIdx = {
                    navBarSelectedItem = it
                }
            )
        },
    ) { innerPadding ->
        val uiState by viewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = ChildScreen.Start.url,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = ChildScreen.Start.url) {
                ChildMainView(viewModel,
                    onClickMission = {
                        navController.navigate(ChildScreen.Mission.url)
                    },
                    onClickCoupon ={
                        navController.navigate(ChildScreen.Coupon.url)
                    }
                )
            }
            composable(route = ChildScreen.StarLink.url) {
                StarLink(viewModel)
            }
            composable(route = ChildScreen.Contents.url) {
                // 컨텐츠 함수
            }
            composable(route = ChildScreen.Coupon.url) {
                // 쿠폰 함수
                CouponScreen(viewModel)
            }
            composable(route = ChildScreen.Mission.url) {
                // 미션 함수
                ChildMission(viewModel)
            }
        }
    }
}


@RequiresApi(Build.VERSION_CODES.O)
//@Composable
@SuppressLint("UnspecifiedImmutableFlag")
fun setAlarm(context: Context, targetBedTime:String){
//    val timeSec = System.currentTimeMillis() + 5000
//    val temp = "12:25"
    Log.d("설정 수면 시간", targetBedTime)
    if(targetBedTime != "") {
        val now = LocalDateTime.now()
        val timeArr = targetBedTime.split(":")
//        val timeArr = temp.split(":")
        val hour = timeArr[0]
        val minute = timeArr[1]
        val temptime = LocalDateTime.of(now.year, now.monthValue, now.dayOfMonth, hour.toInt(), minute.toInt(), 0)
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val intent = Intent(context, ChildAlarm::class.java)
        val pendingIntent = PendingIntent.getBroadcast(context,0,intent, PendingIntent.FLAG_MUTABLE)
        alarmManager.set(AlarmManager.RTC_WAKEUP, temptime.atZone(ZoneId.systemDefault()).toInstant()?.toEpochMilli() ?: 0, pendingIntent)
    }
}

@RequiresApi(Build.VERSION_CODES.S)
@Composable
@Preview(showSystemUi = true)
fun preview(){
    ChildApp()
}

