package com.example.workoutlogv2.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "personal_records")
data class PersonalRecord(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val exerciseName: String,
    val weightKg: Double,
    val reps: Int,
    val date: String = LocalDate.now().toString()
)
