package com.example.skillforge.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val LightColorScheme = lightColorScheme(
    primary = TealPrimary,
    background = CreamBackground,
    surface = CardBackground,
    onPrimary = CardBackground,
    onBackground = TextCharcoal,
    onSurface = TextCharcoal
)

@Composable
fun SkillforgeTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = LightColorScheme,
        typography = Typography,
        content = content
    )
}
