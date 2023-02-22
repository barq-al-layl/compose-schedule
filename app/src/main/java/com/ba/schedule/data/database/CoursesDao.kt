package com.ba.schedule.data.database

import androidx.room.*
import com.ba.schedule.data.entity.CourseEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CoursesDao : BaseDao<CourseEntity> {

    @Query("SELECT * FROM courses")
    fun getAll(): Flow<List<CourseEntity>>

    @Query("SELECT * FROM courses WHERE id = :id")
    suspend fun getBy(id: Int): CourseEntity?
}