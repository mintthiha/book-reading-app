package com.example.bookreadingapp.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.bookreadingapp.R

val Merriweather = FontFamily(
        Font(R.font.merriweather_bold)
)

val Roboto = FontFamily(
        Font(R.font.roboto_light),
        Font(R.font.roboto_medium)
)

val Lora = FontFamily(
        Font(R.font.lora_regular_italic)
)

val Typography = Typography(
        // Usually for app name
        displayLarge = TextStyle(
                fontFamily = Merriweather,
                fontWeight = FontWeight.Normal,
                fontSize = 36.sp
        ),
        displayMedium = TextStyle(
                fontFamily = Merriweather,
                fontWeight = FontWeight.Normal,
                fontSize = 30.sp
        ),
        displaySmall = TextStyle(
                fontFamily = Merriweather,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp
        ),
        // For headlines (same size as titles), bold
        headlineLarge = TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 32.sp
        ),
        headlineMedium = TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 28.sp
        ),
        headlineSmall = TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Bold,
                fontSize = 24.sp
        ),
        // For titles (same size as headlines), not bold
        titleLarge = TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 32.sp
        ),
        titleMedium = TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 28.sp
        ),
        titleSmall = TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 24.sp
        ),
        // For normal text, not bold
        bodyLarge = TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp
        ),
        bodyMedium = TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 14.sp
        ),
        bodySmall = TextStyle(
                fontFamily = Roboto,
                fontWeight = FontWeight.Normal,
                fontSize = 10.sp
        )
)

// Custom quote style on top of Typography's existing styles
val Typography.quote: TextStyle
        get() = TextStyle(
                fontFamily = Lora,
                fontWeight = FontWeight.Normal,
                fontSize = 18.sp,
                fontStyle = FontStyle.Italic
        )