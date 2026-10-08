package com.prayagi.netraassistant.ui

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val Dark = darkColorScheme(primary = Color(0xFF7C4DFF), secondary = Color(0xFFFFB300))
private val Light = lightColorScheme(primary = Color(0xFF5E35B1), secondary = Color(0xFFFF8F00))

@Composable
fun NetraTheme(content: @Composable () -> Unit) {
    MaterialTheme(colorScheme = if (isSystemInDarkTheme()) Dark else Light, content = content)
}
