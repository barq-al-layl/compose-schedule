package com.ba.schedule.data.database

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Transaction
import com.ba.schedule.data.entity.CourseWithExams
import com.ba.schedule.data.entity.ExamEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ExamsDao : BaseDao<ExamEntity> {

    @Transaction
    @Query("SELECT * FROM courses")
    fun getExamAndCourses(): Flow<List<CourseWithExams>>

    @Transaction
    @Query("SELECT * FROM courses WHERE id = :id")
    suspend fun getCourseWithExamsBy(id: Int): CourseWithExams?
}