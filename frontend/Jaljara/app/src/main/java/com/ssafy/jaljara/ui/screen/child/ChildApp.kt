package com.ssafy.jaljara.ui.screen.child

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.annotation.StringRes
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ssafy.jaljara.R
import com.ssafy.jaljara.data.notUsedCoupons
import com.ssafy.jaljara.data.usedCoupons
import com.ssafy.jaljara.ui.screen.ParentMain
import com.ssafy.jaljara.ui.screen.parent.*
import com.ssafy.jaljara.ui.vm.ParentViewModel


@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun ChildApp(
    modifier: Modifier = Modifier
) {
    Column(
        Modifier.background(color = Color(171,152,226)),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ChildTitle("오늘의 미션", modifier.padding(top = 10.dp, bottom = 10.dp))
//        CouponScreen(Modifier.background(Color.Transparent))
        ChildMission()
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Composable
@Preview(showSystemUi = true)
fun preview(){
    ChildApp()
}