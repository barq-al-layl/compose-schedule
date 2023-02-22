package com.ba.schedule.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ba.schedule.data.entity.CourseEntity
import com.ba.schedule.data.entity.LectureEntity

@Database(
    entities = [
        CourseEntity::class,
        LectureEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
abstract class ScheduleDatabase : RoomDatabase() {

    abstract val coursesDao: CoursesDao
    abstract val lecturesDao: LecturesDao

    companion object {
        const val DATABASE_NAME = "schedule_database"
    }
}