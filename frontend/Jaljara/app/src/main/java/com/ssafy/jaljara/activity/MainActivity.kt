package com.ssafy.jaljara.activity

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.view.WindowCompat
import com.ssafy.jaljara.ui.screen.child.ChildApp
import com.ssafy.jaljara.ui.screen.common.LandingApp
import com.ssafy.jaljara.ui.screen.parent.ParentApp
import com.ssafy.jaljara.ui.theme.JaljaraTheme
import com.ssafy.jaljara.ui.vm.ChildViewModel
import com.ssafy.jaljara.ui.vm.LandingViewModel
import com.ssafy.jaljara.ui.vm.ParentViewModel

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JaljaraTheme {
//                ChildApp()
//                ParentApp()
                LandingApp()
            }
        }
    }
}
