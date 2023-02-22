package com.ba.schedule.di

import android.content.Context
import androidx.room.Room
import com.ba.schedule.data.database.ScheduleDatabase
import com.ba.schedule.data.repository.CoursesRepositoryImpl
import com.ba.schedule.data.repository.LecturesRepositoryImpl
import com.ba.schedule.domain.repository.CoursesRepository
import com.ba.schedule.domain.repository.LecturesRepository
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
    fun database(@ApplicationContext context: Context): ScheduleDatabase {
        return Room.databaseBuilder(
            context,
            ScheduleDatabase::class.java,
            ScheduleDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    @Singleton
    fun coursesRepository(db: ScheduleDatabase): CoursesRepository {
        return CoursesRepositoryImpl(db.coursesDao)
    }

    @Provides
    @Singleton
    fun lecturesRepository(db: ScheduleDatabase): LecturesRepository {
        return LecturesRepositoryImpl(db.lecturesDao)
    }
}