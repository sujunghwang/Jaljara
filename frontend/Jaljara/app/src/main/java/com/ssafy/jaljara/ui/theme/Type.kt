package com.ssafy.jaljara.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ssafy.jaljara.R

val customFont = FontFamily(
    Font(R.font.dongle)
)

// Set of Material typography styles to start with
val JaljaraTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = customFont,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = customFont,
        fontWeight = FontWeight.Bold,
        fontSize = 30.sp,
        color = Color.Black
    ),
    titleSmall = TextStyle(
        fontFamily = customFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = customFont,
        fontWeight = FontWeight.Bold,
        fontSize = 48.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = customFont,
        fontSize = 22.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = customFont,
        fontSize = 22.sp,
        color = Color.Black
    ),

)