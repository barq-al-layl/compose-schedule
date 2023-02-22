package com.ba.schedule.data.repository

import com.ba.schedule.data.database.CoursesDao
import com.ba.schedule.data.entity.CourseEntity
import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.repository.CoursesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class CoursesRepositoryImpl(private val dao: CoursesDao) : CoursesRepository {

    override fun getAll(): Flow<List<Course>> {
        return dao.getAll().map { courses ->
            courses.sortedWith(
                compareBy({ it.name.length }, { it.name }),
            ).map { it.toCourse() }
        }
    }

    override suspend fun getById(id: Int): Course? {
        return dao.getBy(id)?.toCourse()
    }

    override suspend fun add(course: Course) {
        dao.insert(CourseEntity.fromCourse(course))
    }

    override suspend fun delete(course: Course) {
        dao.delete(CourseEntity.fromCourse(course))
    }
}