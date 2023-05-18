package com.ssafy.jaljara.ui.screen.child


import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.twotone.ContentCopy
import androidx.compose.material.icons.twotone.ContentCut
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.style.TextOverflow
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ssafy.jaljara.ui.screen.parent.*
import com.ssafy.jaljara.ui.vm.ChildViewModel
import com.ssafy.jaljara.ui.vm.ParentViewModel


enum class CouponStatus(val title: String, val icon: ImageVector) {
    Coupon(title = "사용하지 않은 쿠폰", icon = Icons.TwoTone.ContentCopy),
    UsedCoupon(title = "사용한 쿠폰", icon = Icons.TwoTone.ContentCut)
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
                icon = { Icon(item.icon, contentDescription = item.name) },
                label = { Text(item.title, style = MaterialTheme.typography.titleSmall, overflow = TextOverflow.Clip) },
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
    navController: NavHostController = rememberNavController(),
    userId: Long
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
                    childViewModel.getNotUsedCoupon(userId)
                    items(coupons){
                        NotUsedCoupon(childViewModel = childViewModel, coupon = it)
                    }
                }
            }
            composable(route = CouponStatus.UsedCoupon.name) {
                LazyColumn() {
                    val coupons = childViewModel.usedCouponResponse
                    childViewModel.getUsedCoupon(userId)
                    items(coupons){
                        UsedCoupon(coupon = it)
                    }
                }
            }
        }
    }
}
