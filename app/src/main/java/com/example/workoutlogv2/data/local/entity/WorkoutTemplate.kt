package com.example.workoutlogv2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_templates")
data class WorkoutTemplate(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseName: String,
    val defaultSets: Int,
    val defaultReps: Int,
    val defaultWeightKg: Double,
    val notes: String = ""
)
