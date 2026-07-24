package com.example.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val SemsarkColorScheme = darkColorScheme(
    primary = RoyalGold,
    onPrimary = Color.Black,
    primaryContainer = DarkEmeraldElevated,
    onPrimaryContainer = SoftGold,
    secondary = SoftGold,
    onSecondary = DarkEmerald,
    background = DarkEmerald,
    onBackground = TextWhite,
    surface = DarkEmeraldSurface,
    onSurface = TextWhite,
    surfaceVariant = DarkEmeraldCard,
    onSurfaceVariant = TextSecondary,
    outline = DarkGold,
    error = StatusRejected,
    onError = Color.White
)

@Composable
fun SemsarkTheme(
    content: @Composable () -> Unit
) {
    MaterialTheme(
        colorScheme = SemsarkColorScheme,
        typography = Typography,
        content = content
    )
}

@Composable
fun MyApplicationTheme(
    darkTheme: Boolean = true,
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    SemsarkTheme(content = content)
}