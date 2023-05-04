package com.ssafy.jaljara.ui.screen.child

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
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
import com.ssafy.jaljara.ui.vm.ChildViewModel


/**
 * 자녀 앱의 시작점입니다.
 * composable routing 및
 * 공통 bar가 사용됩니다.
 * */

enum class ChildScreen(@StringRes val title: Int, val url: String) {
    Start(title = R.string.main, "/main"),
    StarLink(title = R.string.star_link, "/starlink"),
    Contents(title = R.string.contents, "/contents"),
    Coupon(title = R.string.coupon, "/coupons")
}


data class NavigationInfo(val route: ChildScreen, val icon: ImageVector)
@Composable
fun ChildNavigationBar(

    /** 원하는 아이콘을 찾아서 넣으세요 **/
    items : List<NavigationInfo> = listOf(
        NavigationInfo(ChildScreen.Start, Icons.Filled.Star),
        NavigationInfo(ChildScreen.StarLink, Icons.Filled.Star),
        NavigationInfo(ChildScreen.Contents, Icons.Filled.Star),
        NavigationInfo(ChildScreen.Coupon, Icons.Filled.Star)
    ),
    navController : NavController,
    selectedItem : Int,
    onChangeNavIdx: (Int) -> Unit = {},
) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                icon = { Icon(item.icon, contentDescription = item.route.name) },
                label = { Text(stringResource(id = item.route.title)) },
                selected = selectedItem == index,
                onClick = {
                    navController.navigate(item.route.url)
                    onChangeNavIdx(index)
                }
            )
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildApp(
    viewModel: ChildViewModel = viewModel(),
    modifier: Modifier = Modifier,
    navController: NavHostController = rememberNavController()
) {
    // 하단 네비게이션 선택 애니메이션 용
    var navBarSelectedItem by rememberSaveable { mutableStateOf(0) }

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
                ChildMainView(viewModel)
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
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun preview(){
    ChildApp()
}
