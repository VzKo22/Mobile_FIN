package com.example.workoutlogv2.ui

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ListAlt
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.ui.graphics.vector.ImageVector

sealed class Screen(val route: String, val label: String, val icon: ImageVector) {
    object Home : Screen("home", "Today", Icons.Default.FitnessCenter)
    object AddWorkout : Screen("add_workout", "Log", Icons.Default.Add)
    object History : Screen("history", "History", Icons.Default.DateRange)
    object Records : Screen("records", "PRs", Icons.Default.EmojiEvents)
    object Templates : Screen("templates", "Templates", Icons.AutoMirrored.Filled.ListAlt)
}

val bottomNavScreens = listOf(
    Screen.Home,
    Screen.AddWorkout,
    Screen.History,
    Screen.Records,
    Screen.Templates
)
