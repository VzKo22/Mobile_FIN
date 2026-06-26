package com.example.workoutlogv2.ui.screens.home

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.workoutlogv2.ui.components.WorkoutCard
import com.example.workoutlogv2.viewmodel.WorkoutViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import androidx.compose.ui.platform.LocalLocale

@Composable
fun HomeScreen(viewModel: WorkoutViewModel) {
    val workouts by viewModel.todayWorkouts.collectAsState()
    val today = LocalDate.now()
    val formatter = DateTimeFormatter.ofPattern("EEEE, MMMM d", LocalLocale.current.platformLocale)

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))

        // Header
        Text(
            text = "Today's Session",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = today.format(formatter),
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Summary bar
        if (workouts.isNotEmpty()) {
            SummaryRow(
                totalSets = workouts.sumOf { it.sets },
                totalExercises = workouts.size,
                totalVolume = workouts.sumOf { it.sets * it.reps * it.weightKg }
            )
            Spacer(modifier = Modifier.height(16.dp))
        }

        // Workout list
        if (workouts.isEmpty()) {
            EmptyState()
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                items(workouts, key = { it.id }) { workout ->
                    WorkoutCard(
                        workout = workout,
                        onDelete = { viewModel.deleteWorkout(it) }
                    )
                }
            }
        }
    }
}

@SuppressLint("DefaultLocale")
@Composable
private fun SummaryRow(totalSets: Int, totalExercises: Int, totalVolume: Double) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        SummaryCard("Exercises", "$totalExercises", Modifier.weight(1f))
        SummaryCard("Total Sets", "$totalSets", Modifier.weight(1f))
        SummaryCard("Volume", "${String.format("%.0f", totalVolume)} kg", Modifier.weight(1f))
    }
}

@Composable
private fun SummaryCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        modifier = modifier,
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = value,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.primary
            )
            Text(
                text = label,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}

@Composable
private fun EmptyState() {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 80.dp),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text(text = "🏋️", style = MaterialTheme.typography.headlineLarge)
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "No workouts logged yet today.",
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = "Tap Log to add your first exercise.",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
