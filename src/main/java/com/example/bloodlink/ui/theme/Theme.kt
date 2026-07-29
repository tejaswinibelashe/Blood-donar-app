package com.example.bloodlink.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = LifeFlowRed,
    onPrimary = White,
    primaryContainer = LifeFlowLightRed,
    onPrimaryContainer = LifeFlowRed,
    secondary = LifeFlowDarkBlue,
    onSecondary = White,
    background = LifeFlowBg,
    onBackground = LifeFlowDarkBlue,
    surface = White,
    onSurface = LifeFlowDarkBlue,
    outline = LifeFlowGrey,
    surfaceVariant = LifeFlowInputBg
)

@Composable
fun BloodLinkTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
