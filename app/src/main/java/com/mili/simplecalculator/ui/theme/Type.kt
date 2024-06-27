package com.mili.simplecalculator.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.mili.simplecalculator.R

val fontFamily = FontFamily(
    Font(R.font.worksans_light, FontWeight.Light),
    Font(R.font.worksans_regular, FontWeight.Normal),
    Font(R.font.worksans_medium, FontWeight.Medium),
    Font(R.font.worksans_semibold, FontWeight.SemiBold),
    Font(R.font.worksans_bold, FontWeight.Bold),
)
// Set of Material typography styles to start with
val Typography = Typography(
    displayMedium = TextStyle(
        fontFamily = fontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),
)