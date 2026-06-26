package com.example.workoutlogv2.data.local.dao

import androidx.room.*
import com.example.workoutlogv2.data.local.entity.WorkoutEntry
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutDao {

    @Query("SELECT * FROM workout_entries ORDER BY date DESC, id DESC")
    fun getAllWorkouts(): Flow<List<WorkoutEntry>>

    @Query("SELECT * FROM workout_entries WHERE date = :date ORDER BY id DESC")
    fun getWorkoutsByDate(date: String): Flow<List<WorkoutEntry>>

    @Query("SELECT DISTINCT date FROM workout_entries ORDER BY date DESC")
    fun getAllDates(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkout(workout: WorkoutEntry)

    @Delete
    suspend fun deleteWorkout(workout: WorkoutEntry)

    @Query("DELETE FROM workout_entries WHERE id = :id")
    suspend fun deleteWorkoutById(id: Int)
}
