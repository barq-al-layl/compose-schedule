package com.ba.schedule.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.ba.schedule.data.entity.CourseEntity
import com.ba.schedule.data.entity.ExamEntity
import com.ba.schedule.data.entity.LectureEntity

@Database(
    entities = [
        CourseEntity::class,
        LectureEntity::class,
        ExamEntity::class,
    ],
    version = 1,
    exportSchema = false,
)
@TypeConverters(Converters::class)
abstract class ScheduleDatabase : RoomDatabase() {

    abstract val coursesDao: CoursesDao
    abstract val lecturesDao: LecturesDao
    abstract val examsDao: ExamsDao

    companion object {
        const val DATABASE_NAME = "schedule_database"
    }
}