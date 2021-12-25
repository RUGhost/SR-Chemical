package com.aminul.companion.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material.MaterialTheme
import androidx.compose.material.darkColors
import androidx.compose.material.lightColors
import androidx.compose.runtime.Composable

private val DarkColorPalette = darkColors(
    primary = DarkGreen,
    primaryVariant = SeaGreen,
    onPrimary = White,
    secondary = DarkOrange,
    secondaryVariant = DarkBlue,
    onSecondary = Black,
    error = Red,
    onError = White,
    background = Black,
    onBackground = White,
    surface = LightBlack,
    onSurface = White
)

private val LightColorPalette = lightColors(
    primary = ForestGreen,
    primaryVariant = MediumSeaGreen,
    onPrimary = White,
    secondary = Orange,
    secondaryVariant = LightBlue,
    onSecondary = Black,
    error = DarkRed,
    onError = White,
    background = White,
    onBackground = Black,
    surface = LightGray,
    onSurface = Black
)

@Composable
fun CompanionTheme(darkTheme: Boolean = isSystemInDarkTheme(), content: @Composable() () -> Unit) {
    val colors = if (darkTheme) {
        DarkColorPalette
    } else {
        LightColorPalette
    }

    MaterialTheme(
        colors = colors,
        typography = Typography,
        shapes = Shapes,
        content = content
    )
}