package com.example.waterminder.ui.theme


import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColors = lightColorScheme(
    primary = WaterBlue,
    secondary = WaterBlueDark,
    background = Color(0xFFE6F4FF),
    surface = Color(0xFFE6F4FF)
)

private val DarkColors = darkColorScheme(
    primary = WaterBlue,
    secondary = WaterBlueLight,
    background = Color(0xFF001F2E),
    surface = Color(0xFF001F2E)
)

@Composable
fun WaterMinderTheme(
    darkTheme: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = if (darkTheme) DarkColors else LightColors,
        content = content
    )
}

