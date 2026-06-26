package com.example.workoutlogv2

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.workoutlogv2.ui.Screen
import com.example.workoutlogv2.ui.bottomNavScreens
import com.example.workoutlogv2.ui.screens.addworkout.AddWorkoutScreen
import com.example.workoutlogv2.ui.screens.history.HistoryScreen
import com.example.workoutlogv2.ui.screens.home.HomeScreen
import com.example.workoutlogv2.ui.screens.records.PersonalRecordsScreen
import com.example.workoutlogv2.ui.screens.templates.TemplatesScreen
import com.example.workoutlogv2.ui.theme.WorkoutLogTheme
import com.example.workoutlogv2.viewmodel.WorkoutViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            WorkoutLogTheme {
                WorkoutLogNavHost()
            }
        }
    }
}

@Composable
fun WorkoutLogNavHost() {
    val navController = rememberNavController()
    val viewModel: WorkoutViewModel = hiltViewModel()

    Scaffold(
        bottomBar = {
            NavigationBar {
                val navBackStackEntry by navController.currentBackStackEntryAsState()
                val currentDestination = navBackStackEntry?.destination
                bottomNavScreens.forEach { screen ->
                    NavigationBarItem(
                        icon = { Icon(screen.icon, contentDescription = screen.label) },
                        label = { Text(screen.label) },
                        selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                        onClick = {
                            navController.navigate(screen.route) {
                                popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                                launchSingleTop = true
                                restoreState = true
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = Screen.Home.route,
            modifier = Modifier.padding(innerPadding)
        ) {
            composable(Screen.Home.route) { HomeScreen(viewModel) }
            composable(Screen.AddWorkout.route) { AddWorkoutScreen(viewModel) }
            composable(Screen.History.route) { HistoryScreen(viewModel) }
            composable(Screen.Records.route) { PersonalRecordsScreen(viewModel) }
            composable(Screen.Templates.route) {
                TemplatesScreen(
                    viewModel = viewModel,
                    onUseTemplate = {
                        navController.navigate(Screen.AddWorkout.route) {
                            popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                            launchSingleTop = true
                        }
                    }
                )
            }
        }
    }
}
