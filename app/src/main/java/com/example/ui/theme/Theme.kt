package com.example.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable

private val DarkColorScheme = darkColorScheme(
    primary = TogetherPrimaryGreen,
    onPrimary = TogetherCardBg,
    primaryContainer = TogetherDeepGreen,
    onPrimaryContainer = TogetherCardBg,
    secondary = TogetherWarmPink,
    onSecondary = TogetherCardBg,
    tertiary = TogetherMutedPurple,
    onTertiary = TogetherCardBg,
    background = TogetherDarkBg,
    onBackground = TogetherDarkPrimaryText,
    surface = TogetherDarkCard,
    onSurface = TogetherDarkPrimaryText,
    surfaceVariant = TogetherDarkCard,
    onSurfaceVariant = TogetherDarkSecondaryText,
    outline = TogetherDarkBorder
)

private val LightColorScheme = lightColorScheme(
    primary = TogetherPrimaryGreen,
    onPrimary = TogetherCardBg,
    primaryContainer = TogetherDeepGreen,
    onPrimaryContainer = TogetherCardBg,
    secondary = TogetherWarmPink,
    onSecondary = TogetherCardBg,
    tertiary = TogetherMutedPurple,
    onTertiary = TogetherCardBg,
    background = TogetherBackground,
    onBackground = TogetherPrimaryText,
    surface = TogetherCardBg,
    onSurface = TogetherPrimaryText,
    surfaceVariant = TogetherCardBg,
    onSurfaceVariant = TogetherSecondaryText,
    outline = TogetherBorder
)

@Composable
fun TogetherSyncTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
