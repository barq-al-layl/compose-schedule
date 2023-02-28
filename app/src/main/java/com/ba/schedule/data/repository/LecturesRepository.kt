package com.ba.schedule.data.repository

import com.ba.schedule.data.database.LecturesDao
import com.ba.schedule.data.entity.LectureEntity
import com.ba.schedule.model.Lecture
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class LecturesRepository @Inject constructor(
    private val dao: LecturesDao,
) {
    fun getAll(): Flow<List<Lecture>> {
        return dao.getCourseWithLectures().map {
            it.fold(listOf()) { lectures, courseWithLectures ->
                lectures + courseWithLectures.toLectures()
            }
        }
    }

    suspend fun add(lecture: Lecture) {
        dao.insert(LectureEntity.fromLecture(lecture))
    }

    suspend fun remove(lecture: Lecture) {
        dao.delete(LectureEntity.fromLecture(lecture))
    }
}