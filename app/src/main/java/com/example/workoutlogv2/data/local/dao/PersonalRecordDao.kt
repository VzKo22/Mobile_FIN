package com.example.workoutlogv2.data.local.dao

import androidx.room.*
import com.example.workoutlogv2.data.local.entity.PersonalRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface PersonalRecordDao {

    @Query("SELECT * FROM personal_records ORDER BY exerciseName ASC")
    fun getAllRecords(): Flow<List<PersonalRecord>>

    @Query("SELECT * FROM personal_records WHERE exerciseName = :name LIMIT 1")
    suspend fun getRecordForExercise(name: String): PersonalRecord?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecord(record: PersonalRecord)

    @Query("DELETE FROM personal_records WHERE id = :id")
    suspend fun deleteRecord(id: Int)
}
