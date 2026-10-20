package com.duoc.traductor.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import com.traductortuco.R

val ElMessiri = FontFamily(
    Font(R.font.el_messiri_regular, FontWeight.Normal),
    Font(R.font.el_messiri_semibold, FontWeight.SemiBold),
    Font(R.font.el_messiri_bold, FontWeight.Bold)
)

val Nunito = FontFamily(
    Font(R.font.nunito_regular, FontWeight.Normal),
    Font(R.font.nunito_medium, FontWeight.Medium),
    Font(R.font.nunito_bold, FontWeight.Bold)
)

private val base = Typography()

val Typography = Typography(
    displayLarge = base.displayLarge.copy(fontFamily = ElMessiri, fontWeight = FontWeight.Bold),
    displayMedium = base.displayMedium.copy(fontFamily = ElMessiri, fontWeight = FontWeight.Bold),
    displaySmall = base.displaySmall.copy(fontFamily = ElMessiri, fontWeight = FontWeight.Bold),
    headlineLarge = base.headlineLarge.copy(fontFamily = ElMessiri, fontWeight = FontWeight.SemiBold),
    headlineMedium = base.headlineMedium.copy(fontFamily = ElMessiri, fontWeight = FontWeight.SemiBold),
    headlineSmall = base.headlineSmall.copy(fontFamily = ElMessiri, fontWeight = FontWeight.SemiBold),
    titleLarge = base.titleLarge.copy(fontFamily = ElMessiri, fontWeight = FontWeight.SemiBold),
    titleMedium = base.titleMedium.copy(fontFamily = ElMessiri, fontWeight = FontWeight.SemiBold),
    titleSmall = base.titleSmall.copy(fontFamily = ElMessiri, fontWeight = FontWeight.SemiBold),
    bodyLarge = base.bodyLarge.copy(fontFamily = Nunito),
    bodyMedium = base.bodyMedium.copy(fontFamily = Nunito),
    bodySmall = base.bodySmall.copy(fontFamily = Nunito),
    labelLarge = base.labelLarge.copy(fontFamily = Nunito, fontWeight = FontWeight.Medium),
    labelMedium = base.labelMedium.copy(fontFamily = Nunito, fontWeight = FontWeight.Medium),
    labelSmall = base.labelSmall.copy(fontFamily = Nunito, fontWeight = FontWeight.Medium)
)