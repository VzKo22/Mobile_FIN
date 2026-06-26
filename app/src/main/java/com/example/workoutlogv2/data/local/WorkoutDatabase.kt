package com.example.workoutlogv2.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.workoutlogv2.data.local.dao.PersonalRecordDao
import com.example.workoutlogv2.data.local.dao.WorkoutDao
import com.example.workoutlogv2.data.local.dao.WorkoutTemplateDao
import com.example.workoutlogv2.data.local.entity.PersonalRecord
import com.example.workoutlogv2.data.local.entity.WorkoutEntry
import com.example.workoutlogv2.data.local.entity.WorkoutTemplate

@Database(
    entities = [WorkoutEntry::class, PersonalRecord::class, WorkoutTemplate::class],
    version = 2,
    exportSchema = false
)
abstract class WorkoutDatabase : RoomDatabase() {
    abstract fun workoutDao(): WorkoutDao
    abstract fun personalRecordDao(): PersonalRecordDao
    abstract fun workoutTemplateDao(): WorkoutTemplateDao
}
