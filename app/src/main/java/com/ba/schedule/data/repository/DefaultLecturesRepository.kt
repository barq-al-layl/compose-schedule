package com.ba.schedule.data.repository

import com.ba.schedule.data.database.LecturesDao
import com.ba.schedule.data.entity.LectureEntity
import com.ba.schedule.domain.model.Lecture
import com.ba.schedule.domain.repository.LecturesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultLecturesRepository @Inject constructor(
    private val dao: LecturesDao,
) : LecturesRepository {
    override fun getAll(): Flow<List<Lecture>> {
        return dao.getCourseWithLectures().map {
            it.fold(listOf()) { lectures, courseWithLectures ->
                lectures + courseWithLectures.toLectures()
            }
        }
    }

    override suspend fun add(lecture: Lecture) {
        dao.insert(LectureEntity.fromLecture(lecture))
    }

    override suspend fun remove(lecture: Lecture) {
        dao.delete(LectureEntity.fromLecture(lecture))
    }
}