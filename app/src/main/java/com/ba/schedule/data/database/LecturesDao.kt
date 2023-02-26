package com.ba.schedule.data.database

import androidx.room.*
import com.ba.schedule.data.entity.CourseWithLectures
import com.ba.schedule.data.entity.LectureEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LecturesDao : BaseDao<LectureEntity> {

    @Transaction
    @Query("SELECT * FROM courses")
    fun getCourseWithLectures(): Flow<List<CourseWithLectures>>
}