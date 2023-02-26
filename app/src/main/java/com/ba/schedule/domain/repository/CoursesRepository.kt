package com.ba.schedule.domain.repository

import com.ba.schedule.domain.model.Course
import kotlinx.coroutines.flow.Flow

interface CoursesRepository {
    fun getAll(): Flow<List<Course>>

    suspend fun getById(id: Int): Course?

    suspend fun add(course: Course): Long

    suspend fun delete(course: Course)

}