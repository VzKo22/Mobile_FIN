package com.example.workoutlogv2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "workout_entries")
data class WorkoutEntry(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseName: String,
    val sets: Int,
    val reps: Int,
    val weightKg: Double,
    val date: String = LocalDate.now().toString(), // stored as ISO string e.g. "2024-06-15"
    val notes: String = ""
)
