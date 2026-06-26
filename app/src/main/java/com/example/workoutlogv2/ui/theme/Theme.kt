package com.example.workoutlogv2.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

private val DarkColorScheme = darkColorScheme(
    primary = ElectricCyan,
    onPrimary = NavyDeep,
    primaryContainer = NavyLight,
    onPrimaryContainer = CyanLight,
    secondary = CyanDark,
    tertiary = GoldPR,
    background = NavyDark,
    onBackground = CardLight,
    surface = CardDark,
    onSurface = CardLight,
    surfaceVariant = NavyMid,
    onSurfaceVariant = SubtleGray,
    outline = DividerColor,
    error = ErrorRed
)

private val LightColorScheme = lightColorScheme(
    primary = CyanDark,
    onPrimary = CardLight,
    primaryContainer = SurfaceLight,
    onPrimaryContainer = NavyDeep,
    secondary = NavyMid,
    tertiary = GoldPR,
    background = SurfaceLight,
    onBackground = NavyDeep,
    surface = CardLight,
    onSurface = NavyDeep,
    surfaceVariant = SurfaceLight,
    onSurfaceVariant = SubtleGray,
    outline = DividerColor,
    error = ErrorRed
)

val WorkoutTypography = Typography(
    headlineLarge = TextStyle(fontWeight = FontWeight.ExtraBold, fontSize = 28.sp, letterSpacing = (-0.5).sp),
    headlineMedium = TextStyle(fontWeight = FontWeight.Bold, fontSize = 22.sp),
    titleLarge = TextStyle(fontWeight = FontWeight.SemiBold, fontSize = 18.sp),
    titleMedium = TextStyle(fontWeight = FontWeight.Medium, fontSize = 15.sp),
    bodyMedium = TextStyle(fontWeight = FontWeight.Normal, fontSize = 14.sp),
    labelSmall = TextStyle(fontWeight = FontWeight.Medium, fontSize = 11.sp, letterSpacing = 0.5.sp)
)

@Composable
fun WorkoutLogTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme
    MaterialTheme(
        colorScheme = colorScheme,
        typography = WorkoutTypography,
        content = content
    )
}
