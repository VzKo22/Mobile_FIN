package com.example.workoutlogv2.ui.screens.addworkout

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.workoutlogv2.viewmodel.WorkoutViewModel
import kotlinx.coroutines.flow.collectLatest

@Composable
fun AddWorkoutScreen(viewModel: WorkoutViewModel) {
    val form by viewModel.formState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.saveSuccess.collectLatest {
            snackbarHostState.showSnackbar("Workout saved! 💪")
        }
    }

    LaunchedEffect(Unit) {
        viewModel.newPrExercise.collectLatest { exercise ->
            snackbarHostState.showSnackbar("🏆 New PR for $exercise!")
        }
    }

    Scaffold(snackbarHost = { SnackbarHost(snackbarHostState) }) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = "Log Exercise", style = MaterialTheme.typography.headlineLarge)

            OutlinedTextField(
                value = form.exerciseName,
                onValueChange = viewModel::onExerciseNameChange,
                label = { Text("Exercise name") },
                placeholder = { Text("e.g. Bench Press") },
                isError = form.nameError != null,
                supportingText = form.nameError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                singleLine = true
            )

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                OutlinedTextField(
                    value = form.sets,
                    onValueChange = viewModel::onSetsChange,
                    label = { Text("Sets") },
                    isError = form.setsError != null,
                    supportingText = form.setsError?.let { { Text(it) } },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
                OutlinedTextField(
                    value = form.reps,
                    onValueChange = viewModel::onRepsChange,
                    label = { Text("Reps") },
                    isError = form.repsError != null,
                    supportingText = form.repsError?.let { { Text(it) } },
                    modifier = Modifier.weight(1f),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true
                )
            }

            OutlinedTextField(
                value = form.weightKg,
                onValueChange = viewModel::onWeightChange,
                label = { Text("Weight (kg)") },
                placeholder = { Text("0.0 for bodyweight") },
                isError = form.weightError != null,
                supportingText = form.weightError?.let { { Text(it) } },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                singleLine = true
            )

            OutlinedTextField(
                value = form.notes,
                onValueChange = viewModel::onNotesChange,
                label = { Text("Notes (optional)") },
                modifier = Modifier.fillMaxWidth().height(100.dp),
                maxLines = 3
            )

            Spacer(modifier = Modifier.height(8.dp))

            Button(
                onClick = viewModel::saveWorkout,
                modifier = Modifier.fillMaxWidth().height(52.dp)
            ) {
                Text(text = "Save Exercise", style = MaterialTheme.typography.titleMedium)
            }

            TextButton(onClick = viewModel::resetForm, modifier = Modifier.fillMaxWidth()) {
                Text("Clear form")
            }

            Spacer(modifier = Modifier.height(80.dp))
        }
    }
}
