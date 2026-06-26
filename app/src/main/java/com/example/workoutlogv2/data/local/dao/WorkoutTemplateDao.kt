package com.example.workoutlogv2.data.local.dao

import androidx.room.*
import com.example.workoutlogv2.data.local.entity.WorkoutTemplate
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutTemplateDao {

    @Query("SELECT * FROM workout_templates ORDER BY exerciseName ASC")
    fun getAllTemplates(): Flow<List<WorkoutTemplate>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTemplate(template: WorkoutTemplate)

    @Query("DELETE FROM workout_templates WHERE id = :id")
    suspend fun deleteTemplate(id: Int)
}
