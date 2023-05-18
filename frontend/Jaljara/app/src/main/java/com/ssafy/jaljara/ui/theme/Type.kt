package com.ssafy.jaljara.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.ssafy.jaljara.R

val customFont = FontFamily(
    Font(R.font.cutelively)
)

// Set of Material typography styles to start with
val JaljaraTypography = Typography(
    titleLarge = TextStyle(
        fontFamily = customFont,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
    ),
    titleMedium = TextStyle(
        fontFamily = customFont,
        fontWeight = FontWeight.Bold,
        fontSize = 24.sp
    ),
    titleSmall = TextStyle(
        fontFamily = customFont,
        fontWeight = FontWeight.Bold,
        fontSize = 16.sp,
    ),
    bodyLarge = TextStyle(
        fontFamily = customFont,
        fontWeight = FontWeight.Bold,
        fontSize = 40.sp,
    ),
    bodyMedium = TextStyle(
        fontFamily = customFont,
        fontSize = 18.sp,
    ),
    bodySmall = TextStyle(
        fontFamily = customFont,
        fontSize = 12.sp,
    ),

)