package com.example.workoutlogv2.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.workoutlogv2.data.local.entity.PersonalRecord
import com.example.workoutlogv2.data.local.entity.WorkoutEntry
import com.example.workoutlogv2.data.local.entity.WorkoutTemplate
import com.example.workoutlogv2.data.repository.WorkoutRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

data class AddWorkoutFormState(
    val exerciseName: String = "",
    val sets: String = "",
    val reps: String = "",
    val weightKg: String = "",
    val notes: String = "",
    val nameError: String? = null,
    val setsError: String? = null,
    val repsError: String? = null,
    val weightError: String? = null,
)

data class AddTemplateFormState(
    val exerciseName: String = "",
    val sets: String = "",
    val reps: String = "",
    val weightKg: String = "",
    val notes: String = ""
)

@HiltViewModel
class WorkoutViewModel @Inject constructor(
    private val repository: WorkoutRepository
) : ViewModel() {

    // --- Home ---
    val todayWorkouts: StateFlow<List<WorkoutEntry>> = repository
        .getWorkoutsByDate(LocalDate.now().toString())
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- History ---
    val allWorkouts: StateFlow<List<WorkoutEntry>> = repository
        .getAllWorkouts()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Personal Records ---
    val personalRecords: StateFlow<List<PersonalRecord>> = repository
        .getAllRecords()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- PR notification ---
    private val _newPrExercise = MutableSharedFlow<String>()
    val newPrExercise = _newPrExercise.asSharedFlow()

    // --- Templates ---
    val templates: StateFlow<List<WorkoutTemplate>> = repository
        .getAllTemplates()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // --- Add Workout Form ---
    private val _formState = MutableStateFlow(AddWorkoutFormState())
    val formState: StateFlow<AddWorkoutFormState> = _formState.asStateFlow()

    private val _saveSuccess = MutableSharedFlow<Boolean>()
    val saveSuccess = _saveSuccess.asSharedFlow()

    // --- Template Form ---
    private val _templateFormState = MutableStateFlow(AddTemplateFormState())
    val templateFormState: StateFlow<AddTemplateFormState> = _templateFormState.asStateFlow()

    // Form field updates
    fun onExerciseNameChange(value: String) = _formState.update { it.copy(exerciseName = value, nameError = null) }
    fun onSetsChange(value: String) = _formState.update { it.copy(sets = value, setsError = null) }
    fun onRepsChange(value: String) = _formState.update { it.copy(reps = value, repsError = null) }
    fun onWeightChange(value: String) = _formState.update { it.copy(weightKg = value, weightError = null) }
    fun onNotesChange(value: String) = _formState.update { it.copy(notes = value) }

    // Fill form from template
    fun applyTemplate(template: WorkoutTemplate) {
        _formState.value = AddWorkoutFormState(
            exerciseName = template.exerciseName,
            sets = template.defaultSets.toString(),
            reps = template.defaultReps.toString(),
            weightKg = template.defaultWeightKg.toString(),
            notes = template.notes
        )
    }

    fun saveWorkout() {
        val state = _formState.value
        var hasError = false

        if (state.exerciseName.isBlank()) {
            _formState.update { it.copy(nameError = "Exercise name is required") }
            hasError = true
        }
        val sets = state.sets.toIntOrNull()
        if (sets == null || sets <= 0) {
            _formState.update { it.copy(setsError = "Enter a valid number of sets") }
            hasError = true
        }
        val reps = state.reps.toIntOrNull()
        if (reps == null || reps <= 0) {
            _formState.update { it.copy(repsError = "Enter a valid number of reps") }
            hasError = true
        }
        val weight = state.weightKg.toDoubleOrNull()
        if (weight == null || weight < 0) {
            _formState.update { it.copy(weightError = "Enter a valid weight") }
            hasError = true
        }
        if (hasError) return

        viewModelScope.launch {
            val existingPr = personalRecords.value.find {
                it.exerciseName.equals(state.exerciseName.trim(), ignoreCase = true)
            }
            val isNewPr = existingPr == null || weight!! > existingPr.weightKg

            repository.addWorkout(
                WorkoutEntry(
                    exerciseName = state.exerciseName.trim(),
                    sets = sets!!,
                    reps = reps!!,
                    weightKg = weight!!,
                    notes = state.notes.trim(),
                    date = LocalDate.now().toString()
                )
            )
            _formState.value = AddWorkoutFormState()
            _saveSuccess.emit(true)
            if (isNewPr) _newPrExercise.emit(state.exerciseName.trim())
        }
    }

    fun deleteWorkout(id: Int) = viewModelScope.launch { repository.deleteWorkoutById(id) }

    fun deleteRecord(id: Int) = viewModelScope.launch { repository.deleteRecord(id) }

    fun resetForm() { _formState.value = AddWorkoutFormState() }

    // Template form updates
    fun onTemplateNameChange(value: String) = _templateFormState.update { it.copy(exerciseName = value) }
    fun onTemplateSetsChange(value: String) = _templateFormState.update { it.copy(sets = value) }
    fun onTemplateRepsChange(value: String) = _templateFormState.update { it.copy(reps = value) }
    fun onTemplateWeightChange(value: String) = _templateFormState.update { it.copy(weightKg = value) }
    fun onTemplateNotesChange(value: String) = _templateFormState.update { it.copy(notes = value) }
    fun resetTemplateForm() { _templateFormState.value = AddTemplateFormState() }

    fun saveTemplate() {
        val state = _templateFormState.value
        if (state.exerciseName.isBlank()) return
        viewModelScope.launch {
            repository.saveTemplate(
                WorkoutTemplate(
                    exerciseName = state.exerciseName.trim(),
                    defaultSets = state.sets.toIntOrNull() ?: 3,
                    defaultReps = state.reps.toIntOrNull() ?: 10,
                    defaultWeightKg = state.weightKg.toDoubleOrNull() ?: 0.0,
                    notes = state.notes.trim()
                )
            )
            _templateFormState.value = AddTemplateFormState()
        }
    }

    fun deleteTemplate(id: Int) = viewModelScope.launch { repository.deleteTemplate(id) }
}
