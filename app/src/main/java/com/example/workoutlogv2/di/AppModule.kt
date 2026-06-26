package com.example.workoutlogv2.di

import android.content.Context
import androidx.room.Room
import com.example.workoutlogv2.data.local.WorkoutDatabase
import com.example.workoutlogv2.data.local.dao.PersonalRecordDao
import com.example.workoutlogv2.data.local.dao.WorkoutDao
import com.example.workoutlogv2.data.local.dao.WorkoutTemplateDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Provides
    @Singleton
    fun provideWorkoutDatabase(@ApplicationContext context: Context): WorkoutDatabase =
        Room.databaseBuilder(
            context,
            WorkoutDatabase::class.java,
            "workout_db"
        )
            .fallbackToDestructiveMigration(false)
        .build()

    @Provides @Singleton
    fun provideWorkoutDao(db: WorkoutDatabase): WorkoutDao = db.workoutDao()

    @Provides @Singleton
    fun providePersonalRecordDao(db: WorkoutDatabase): PersonalRecordDao = db.personalRecordDao()

    @Provides @Singleton
    fun provideWorkoutTemplateDao(db: WorkoutDatabase): WorkoutTemplateDao = db.workoutTemplateDao()
}
