package com.ba.schedule.data.repository

import com.ba.schedule.data.database.LecturesDao
import com.ba.schedule.data.entity.LectureEntity
import com.ba.schedule.domain.model.Lecture
import com.ba.schedule.domain.repository.LecturesRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class LecturesRepositoryImpl(private val dao: LecturesDao) : LecturesRepository {
    override fun getAll(): Flow<List<Lecture>> {
        return dao.getAll().map { value ->
            val lectures = mutableSetOf<Lecture>()
            value.forEach {
                lectures += it.toLectures()
            }
            lectures.toList()
        }
    }

    override suspend fun add(lecture: Lecture) {
        dao.insert(LectureEntity.fromLecture(lecture))
    }

    override suspend fun remove(lecture: Lecture) {
        dao.delete(LectureEntity.fromLecture(lecture))
    }
}