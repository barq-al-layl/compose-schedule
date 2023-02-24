package com.ba.schedule.di

import android.content.Context
import androidx.room.Room
import com.ba.schedule.data.database.ScheduleDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Dispatchers
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object ApplicationModule {

    @Provides
    fun provideCoroutineDispatcher() = Dispatchers.Default

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): ScheduleDatabase {
        return Room.databaseBuilder(
            context,
            ScheduleDatabase::class.java,
            ScheduleDatabase.DATABASE_NAME,
        ).build()
    }

    @Provides
    fun provideCoursesDao(db: ScheduleDatabase) = db.coursesDao

    @Provides
    fun provideLecturesDao(db: ScheduleDatabase) = db.lecturesDao

}