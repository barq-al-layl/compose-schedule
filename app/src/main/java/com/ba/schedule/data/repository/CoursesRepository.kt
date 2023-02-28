package com.ba.schedule.data.repository

import com.ba.schedule.data.database.CoursesDao
import com.ba.schedule.data.entity.CourseEntity
import com.ba.schedule.model.Course
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class CoursesRepository @Inject constructor(
    private val dao: CoursesDao,
) {

    fun getAll(): Flow<List<Course>> {
        return dao.getAll().map { courses ->
            courses.map { it.toCourse() }
        }
    }

    suspend fun getById(id: Int): Course? {
        return dao.getBy(id)?.toCourse()
    }

    suspend fun add(course: Course): Long {
        return dao.insert(CourseEntity.fromCourse(course))
    }

    suspend fun delete(course: Course) {
        dao.delete(CourseEntity.fromCourse(course))
    }
}