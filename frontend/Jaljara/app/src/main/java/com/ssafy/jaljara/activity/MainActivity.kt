package com.ssafy.jaljara.activity

import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import com.ssafy.jaljara.ui.screen.common.LandingApp
import com.ssafy.jaljara.ui.theme.JaljaraTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JaljaraTheme {
                LandingApp()
            }
        }
    }
}
