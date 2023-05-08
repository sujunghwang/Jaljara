package com.ssafy.jaljara

import android.Manifest
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat
import com.ssafy.jaljara.ui.screen.child.ChildApp
import com.ssafy.jaljara.ui.theme.JaljaraTheme
import com.ssafy.jaljara.ui.vm.ChildViewModel
import com.ssafy.jaljara.ui.vm.ParentViewModel

class MainActivity : ComponentActivity() {
    val childViewModel: ChildViewModel by viewModels()
    val parentViewModel: ParentViewModel by viewModels()
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ActivityCompat.requestPermissions(
            this,
            arrayOf(Manifest.permission.RECORD_AUDIO, Manifest.permission.CAMERA),
            0
        )
        setContent {
            JaljaraTheme {
                ChildApp(childViewModel)
//                ParentApp(parentViewModel)
            }
        }
    }
}
