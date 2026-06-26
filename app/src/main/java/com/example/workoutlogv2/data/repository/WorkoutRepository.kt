package com.example.workoutlogv2.data.repository

import com.example.workoutlogv2.data.local.dao.PersonalRecordDao
import com.example.workoutlogv2.data.local.dao.WorkoutDao
import com.example.workoutlogv2.data.local.dao.WorkoutTemplateDao
import com.example.workoutlogv2.data.local.entity.PersonalRecord
import com.example.workoutlogv2.data.local.entity.WorkoutEntry
import com.example.workoutlogv2.data.local.entity.WorkoutTemplate
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class WorkoutRepository @Inject constructor(
    private val workoutDao: WorkoutDao,
    private val prDao: PersonalRecordDao,
    private val templateDao: WorkoutTemplateDao
) {
    // --- Workouts ---
    fun getAllWorkouts(): Flow<List<WorkoutEntry>> = workoutDao.getAllWorkouts()
    fun getWorkoutsByDate(date: String): Flow<List<WorkoutEntry>> = workoutDao.getWorkoutsByDate(date)

    suspend fun addWorkout(workout: WorkoutEntry) {
        workoutDao.insertWorkout(workout)
        checkAndUpdatePR(workout)
    }

    suspend fun deleteWorkoutById(id: Int) = workoutDao.deleteWorkoutById(id)

    // --- Personal Records ---
    fun getAllRecords(): Flow<List<PersonalRecord>> = prDao.getAllRecords()

    private suspend fun checkAndUpdatePR(workout: WorkoutEntry) {
        val existing = prDao.getRecordForExercise(workout.exerciseName)
        if (existing == null || workout.weightKg > existing.weightKg) {
            prDao.insertRecord(
                PersonalRecord(
                    id = existing?.id ?: 0,
                    exerciseName = workout.exerciseName,
                    weightKg = workout.weightKg,
                    reps = workout.reps,
                    date = LocalDate.now().toString()
                )
            )
        }
    }

    suspend fun deleteRecord(id: Int) = prDao.deleteRecord(id)

    // --- Templates ---
    fun getAllTemplates(): Flow<List<WorkoutTemplate>> = templateDao.getAllTemplates()

    suspend fun saveTemplate(template: WorkoutTemplate) = templateDao.insertTemplate(template)

    suspend fun deleteTemplate(id: Int) = templateDao.deleteTemplate(id)
}
