package com.ssafy.jaljara.ui.screen.child


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ssafy.jaljara.R
import com.ssafy.jaljara.ui.screen.parent.*
import com.ssafy.jaljara.ui.vm.ChildViewModel
import com.ssafy.jaljara.ui.vm.ParentViewModel


enum class CouponStatus(@StringRes val title: Int) {
    Coupon(title = R.string.app_name),
    UsedCoupon(title = 2)
}

@Composable
fun ChildCouponNav(
    items : List<CouponStatus> = listOf(CouponStatus.Coupon, CouponStatus.UsedCoupon),
    navController : NavController,
    selectedItem : Int,
    onChangeNavIdx: (Int) -> Unit = {},
) {

    NavigationBar {
        items.forEachIndexed { index, item ->
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



@RequiresApi(Build.VERSION_CODES.O)
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CouponScreen(
    childViewModel : ChildViewModel,
    modifier: Modifier = Modifier,
    viewModel: ParentViewModel = viewModel(),
    navController: NavHostController = rememberNavController()
) {
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = CouponStatus.valueOf("UsedCoupon")


    // 쿠폰 사용 여부 선택창
    var navBarSelectedItem by rememberSaveable { mutableStateOf(0) }

    Scaffold(
        topBar = {
            ChildCouponNav(
                navController = navController,
                selectedItem = navBarSelectedItem,
                onChangeNavIdx = {
                    navBarSelectedItem = it
                }
            )
        },
    ) { innerPadding ->

        NavHost(
            navController = navController,
            startDestination = CouponStatus.Coupon.name,
            modifier = modifier.padding(innerPadding)
        ) {
            composable(route = CouponStatus.Coupon.name) {
                LazyColumn() {
                    val coupons = childViewModel.notUsedCouponResponse
                    childViewModel.getNotUsedCoupon(1)
                    items(coupons){
                        NotUsedCoupon(coupon = it)
                    }
                }
            }
            composable(route = CouponStatus.UsedCoupon.name) {
                LazyColumn() {
                    val coupons = childViewModel.usedCouponResponse
                    childViewModel.getUsedCoupon(1)
                    items(coupons){
                        UsedCoupon(coupon = it)
                    }
                }
            }
        }
    }
}
