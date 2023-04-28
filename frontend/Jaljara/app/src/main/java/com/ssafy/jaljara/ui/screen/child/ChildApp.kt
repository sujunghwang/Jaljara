package com.ssafy.jaljara.ui.screen.child

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.screen.StarLink
import com.ssafy.jaljara.ui.vm.ParentViewModel


/**
 * 자녀 앱의 시작점입니다.
 * composable routing 및
 * 공통 bar가 사용됩니다.
 * */

enum class ChildScreen(@StringRes val title: Int) {
    Start(title = R.string.app_name),
    별자리(title = 2),
    Hate(title = 3),
}

@Composable
fun ParentNavigationBar(
    items : List<ChildScreen> = listOf(ChildScreen.Start,ChildScreen.별자리,ChildScreen.Hate),
    navController : NavController,
    selectedItem : Int,
    onChangeNavIdx: (Int) -> Unit = {},
) {
    NavigationBar {
        items.forEachIndexed { index, item ->
            if(index == 1){
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Star, contentDescription = item.name) },
                    label = { Text(item.name) },
                    selected = selectedItem == index,
                    onClick = {
                        navController.navigate(item.name)
                        onChangeNavIdx(index)
                    }
                )
            }
            else{
                NavigationBarItem(
                    icon = { Icon(Icons.Filled.Favorite, contentDescription = item.name) },
                    label = { Text(item.name) },
                    selected = selectedItem == index,
                    onClick = {
                        navController.navigate(item.name)
                        onChangeNavIdx(index)
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ChildApp(
    modifier: Modifier = Modifier,
    viewModel: ParentViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = ChildScreen.valueOf(
        backStackEntry?.destination?.route ?: ChildScreen.Start.name
    )

    // 하단 네비게이션 선택 애니메이션 용
    var navBarSelectedItem by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        bottomBar = {
            ParentNavigationBar(
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
            startDestination = ChildScreen.Start.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = ChildScreen.Start.name) {
            }
            composable(route = ChildScreen.별자리.name) {
                StarLink()
            }
            composable(route = ChildScreen.Hate.name) {
            }
        }
    }
}

@Composable
@Preview(showSystemUi = true)
fun preview(){
    ChildApp()
}