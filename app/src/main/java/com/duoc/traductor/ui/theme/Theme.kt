package com.duoc.traductor.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val TucoColors = lightColorScheme(
    primary = TucoRust,
    onPrimary = Color.White,
    primaryContainer = TucoCamel,
    onPrimaryContainer = TucoInk,
    secondary = TucoBurntOrange,
    onSecondary = Color.White,
    tertiary = TucoEarth,
    onTertiary = Color.White,
    background = TucoSand,
    onBackground = TucoInk,
    surface = TucoSand,
    onSurface = TucoInk,
    surfaceVariant = TucoCamelDark,
    onSurfaceVariant = TucoInkSoft,
    outline = TucoOutline,
    error = TucoError,
    onError = Color.White
)

@Composable
fun TucoTheme(content: @Composable () -> Unit) {
    MaterialTheme(
        colorScheme = TucoColors,
        typography = Typography,
        content = content
    )
}