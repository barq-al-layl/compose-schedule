package com.ba.schedule.data.repository

import com.ba.schedule.data.database.ExamsDao
import com.ba.schedule.data.entity.ExamEntity
import com.ba.schedule.domain.model.Exam
import com.ba.schedule.domain.repository.ExamsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class DefaultExamsRepository @Inject constructor(
    private val dao: ExamsDao,
) : ExamsRepository {
    override fun getAll(): Flow<List<Exam>> {
        return dao.getExamAndCourses().map {
            it.fold(listOf()) { exams, courseWithExams ->
                exams + courseWithExams.toExams()
            }
        }
    }

    override suspend fun getById(id: Int): List<Exam> {
        return dao.getCourseWithExamsBy(id)?.toExams() ?: emptyList()
    }

    override suspend fun add(exam: Exam) {
        dao.insert(ExamEntity.fromExam(exam))
    }

    override suspend fun remove(exam: Exam) {
        dao.delete(ExamEntity.fromExam(exam))
    }
}