package com.example.workoutlogv2.ui.screens.history

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
import java.util.Locale

@Composable
fun HistoryScreen(viewModel: WorkoutViewModel) {
    val allWorkouts by viewModel.allWorkouts.collectAsState()

    // Group workouts by date
    val grouped = allWorkouts.groupBy { it.date }
    val sortedDates = grouped.keys.sortedDescending()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 20.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        Text(
            text = "History",
            style = MaterialTheme.typography.headlineLarge
        )
        Text(
            text = "${allWorkouts.size} entries across ${sortedDates.size} sessions",
            style = MaterialTheme.typography.bodyMedium,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Spacer(modifier = Modifier.height(16.dp))

        if (sortedDates.isEmpty()) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(bottom = 80.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "No history yet.\nStart logging workouts!",
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        } else {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(8.dp),
                contentPadding = PaddingValues(bottom = 100.dp)
            ) {
                sortedDates.forEach { date ->
                    val workoutsForDate = grouped[date] ?: return@forEach

                    item(key = "header_$date") {
                        DateHeader(dateStr = date)
                    }

                    items(workoutsForDate, key = { it.id }) { workout ->
                        WorkoutCard(
                            workout = workout,
                            onDelete = { viewModel.deleteWorkout(it) },
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }

                    item(key = "divider_$date") {
                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
            }
        }
    }
}

@Composable
private fun DateHeader(dateStr: String) {
    val date = runCatching { LocalDate.parse(dateStr) }.getOrNull()
    val today = LocalDate.now()
    val yesterday = today.minusDays(1)

    val label = when (date) {
        today -> "Today"
        yesterday -> "Yesterday"
        else -> date?.format(DateTimeFormatter.ofPattern("EEEE, MMM d, yyyy", Locale.getDefault()))
            ?: dateStr
    }

    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 6.dp)
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.primary
        )
        Spacer(modifier = Modifier.width(8.dp))
        HorizontalDivider(
            modifier = Modifier.weight(1f),
            color = MaterialTheme.colorScheme.outline
        )
    }
}
