package com.ssafy.jaljara.ui.screen.parent

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CornerSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.CalendarMonth
import androidx.compose.material.icons.twotone.Home
import androidx.compose.material.icons.twotone.KingBed
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navOptions
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.screen.ParentMainView
import com.ssafy.jaljara.ui.theme.DarkNavy
import com.ssafy.jaljara.ui.vm.ParentViewModel
import kotlinx.datetime.toJavaLocalDate
import java.time.LocalDate
import java.time.format.DateTimeFormatter


/**
 * 부모 앱의 시작점입니다.
 * composable routing 및
 * 공통 bar가 사용됩니다.
 * */

enum class ParentScreen(@StringRes val title : Int,  val url: String) {
    Start(title = R.string.main, "/main"),
    SetSleepTime(title = R.string.time_setting, "/set_sleep"),
    SleepCalendar(title = R.string.calendar, "/calendar"),
    SleepLogDetail(title = 3, "/sleep_detail"),
    ParentMission(title = 4, "/parent_mission")

}

data class NavigationInfo(val route: ParentScreen, val icon: ImageVector)
@Composable
fun ParentNavigationBar(

    /** 원하는 아이콘을 찾아서 넣으세요 **/
    items : List<NavigationInfo> = listOf(
        NavigationInfo(ParentScreen.Start, Icons.TwoTone.Home),
        NavigationInfo(ParentScreen.SetSleepTime, Icons.TwoTone.KingBed),
        NavigationInfo(ParentScreen.SleepCalendar, Icons.TwoTone.CalendarMonth),
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
        )
        ),
        containerColor = DarkNavy
    ) {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.route.name) },
                label = { Text(stringResource(id = item.route.title), style = MaterialTheme.typography.titleSmall) },
                selected = selectedItem == index,
                onClick = {
                    navController.navigate(route = item.route.url){
                        popUpTo(ParentScreen.Start.url)
                    }
                    onChangeNavIdx(index)
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentApp(
    viewModel: ParentViewModel = viewModel(),
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // userId
    var userId = viewModel.test!!.userInfo.userId.toLong()
    Log.d("userId","$userId")

    // 하단 네비게이션 선택 애니메이션 용
    var navBarSelectedItem by rememberSaveable { mutableStateOf(0) }

    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        bottomBar = {
            if(uiState.showNavigation){ParentNavigationBar(
                navController = navController,
                selectedItem = navBarSelectedItem,
            )}
        },
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = ParentScreen.Start.url,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = ParentScreen.Start.url) {
                viewModel.setNavShow(true)
                // 부모 메인 페이지
                ParentMainView(viewModel,
                    onClickMissionParent = {
                        navController.navigate(ParentScreen.ParentMission.url)
                    },
                    onClickSetTime ={
                        navController.navigate(ParentScreen.SetSleepTime.url)
                    },
                    userId
                )
                navBarSelectedItem = 0
            }
            composable(route = ParentScreen.SetSleepTime.url) {
                viewModel.setNavShow(true)
                // 목표 수면시간 설정
                SleepTargetScreen(viewModel)
                navBarSelectedItem = 1
            }
            composable(route = ParentScreen.SleepCalendar.url) {
                viewModel.setNavShow(true)
                // 내 아이 수면 달력
                SleepCalenderScreen(onClickDay = {
                    day ->
                    Log.d("캘린더 라우터 클릭", day.toString())

                    val fomatter = DateTimeFormatter.ofPattern("yyyyMMdd")

                    val displayDate = day.toJavaLocalDate().format(fomatter)

                    navController.navigate(ParentScreen.SleepLogDetail.url + "/"+displayDate)
                })
                navBarSelectedItem = 2
            }
            composable(route = ParentScreen.SleepLogDetail.url + "/{formatDate}") {
                    backStackEntry ->
                viewModel.setNavShow(false)
                // 수면 기록 상세 페이지
                SleepLogDetailScreen(formatDate = backStackEntry.arguments?.getString("formatDate")?:"20990513")
            }
            composable(route = ParentScreen.ParentMission.url) {
                    backStackEntry ->
                viewModel.setNavShow(true)
                ParentMission(viewModel)
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showSystemUi = true)
fun preview(){
    ParentApp()
}