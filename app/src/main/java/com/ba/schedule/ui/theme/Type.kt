package com.ba.schedule.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import com.ba.schedule.R

val Roboto = FontFamily(
    Font(
        resId = R.font.roboto_light,
        weight = FontWeight.Light,
        style = FontStyle.Normal,
    ),
    Font(
        resId = R.font.roboto,
        weight = FontWeight.Normal,
        style = FontStyle.Normal,
    ),
    Font(
        resId = R.font.roboto_medium,
        weight = FontWeight.Medium,
        style = FontStyle.Normal,
    ),
    Font(
        resId = R.font.roboto_bold,
        weight = FontWeight.Bold,
        style = FontStyle.Normal,
    ),
)

private val BaseTypography = Typography()

// Set of Material typography styles to start with
val Typography = Typography(
    displayLarge = BaseTypography.displayLarge.copy(fontFamily = Roboto),
    displayMedium = BaseTypography.displayMedium.copy(fontFamily = Roboto),
    displaySmall = BaseTypography.displaySmall.copy(fontFamily = Roboto),

    headlineLarge = BaseTypography.headlineLarge.copy(fontFamily = Roboto),
    headlineMedium = BaseTypography.headlineMedium.copy(fontFamily = Roboto),
    headlineSmall = BaseTypography.headlineSmall.copy(fontFamily = Roboto),

    titleLarge = BaseTypography.titleLarge.copy(fontFamily = Roboto),
    titleMedium = BaseTypography.titleMedium.copy(fontFamily = Roboto),
    titleSmall = BaseTypography.titleSmall.copy(fontFamily = Roboto),

    bodyLarge = BaseTypography.bodyLarge.copy(fontFamily = Roboto),
    bodyMedium = BaseTypography.bodyMedium.copy(fontFamily = Roboto),
    bodySmall = BaseTypography.bodySmall.copy(fontFamily = Roboto),

    labelLarge = BaseTypography.labelLarge.copy(fontFamily = Roboto),
    labelMedium = BaseTypography.labelMedium.copy(fontFamily = Roboto),
    labelSmall = BaseTypography.labelSmall.copy(fontFamily = Roboto),
)

