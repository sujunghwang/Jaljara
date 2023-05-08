package com.ssafy.jaljara.ui.theme

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.ssafy.jaljara.R

@Composable
fun JaljaraTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = JaljaraColorScheme,
        typography = JaljaraTypography,
        content = {
            Box(modifier = Modifier.fillMaxSize()) {
                Image(
                    painter = painterResource(R.drawable.night_forest_bg),
                    contentDescription = "night forest background",
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier.fillMaxSize()
                )
                content()
            }
        }
    )
}