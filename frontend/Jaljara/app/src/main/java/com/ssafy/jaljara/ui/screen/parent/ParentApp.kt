package com.ssafy.jaljara.ui.screen.parent

import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.screen.ParentMain
import com.ssafy.jaljara.ui.vm.ParentViewModel


/**
 * 부모 앱의 시작점입니다.
 * composable routing 및
 * 공통 bar가 사용됩니다.
 * */

enum class ParentScreen(@StringRes val title : Int) {
    Start(title = R.string.main),
    SetSleepTime(title = R.string.time_setting),
    SleepCalendar(title = R.string.calendar),
    SleepLogDetail(title = 3)
}

@Composable
fun ParentNavigationBar(
    items : List<ParentScreen> = listOf(ParentScreen.Start,ParentScreen.SetSleepTime,ParentScreen.SleepCalendar),
    navController : NavController,
    selectedItem : Int,
) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(Icons.Filled.Favorite, contentDescription = item.name) },
                label = { Text(stringResource(id = item.title)) },
                selected = selectedItem == index,
                onClick = {
                    navController.navigate(item.name){
                        /* 새로 렌더링 되는게 아니라
                         이전 상태 저장*/
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ParentApp(
    modifier: Modifier = Modifier,
    viewModel: ParentViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {

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
            startDestination = ParentScreen.Start.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = ParentScreen.Start.name) {
                viewModel.setNavShow(true)
                // 부모 메인 페이지
                ParentMain()
                navBarSelectedItem = 0
            }
            composable(route = ParentScreen.SetSleepTime.name) {
                viewModel.setNavShow(true)
                // 목표 수면시간 설정
                SetTimeScreen()
                navBarSelectedItem = 1
            }
            composable(route = ParentScreen.SleepCalendar.name) {
                viewModel.setNavShow(true)
                // 내 아이 수면 달력
                SleepCalenderScreen(onClickDay = {
                    day ->
                    Log.d("캘린더 라우터 클릭", day.toString())
                    val sb = StringBuilder()

                    // yyyyMMdd format
                    val yyyyMMdd = sb.append(day.year).append(day.monthNumber).append(day.dayOfMonth)
                    navController.navigate(ParentScreen.SleepLogDetail.name + "/"+yyyyMMdd)
                })
                navBarSelectedItem = 2
            }
            composable(route = ParentScreen.SleepLogDetail.name + "/{formatDate}") {
                    backStackEntry ->
                viewModel.setNavShow(false)
                // 수면 기록 상세 페이지
                SleepLogDetailScreen(backStackEntry.arguments?.getString("formatDate"))
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