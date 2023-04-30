package com.ssafy.jaljara.ui.screen

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.ssafy.jaljara.ui.screen.child.ChildApp
import com.ssafy.jaljara.ui.screen.parent.ParentApp

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun JaljaraApp(){
//    ParentApp()
    ChildApp(modifier = Modifier)
}

