package com.ba.schedule.data.repository

import com.ba.schedule.data.database.ExamsDao
import com.ba.schedule.data.entity.ExamEntity
import com.ba.schedule.model.Exam
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ExamsRepository @Inject constructor(
    private val dao: ExamsDao,
) {
    fun getAll(): Flow<List<Exam>> {
        return dao.getExamAndCourses().map {
            it.fold(listOf()) { exams, courseWithExams ->
                exams + courseWithExams.toExams()
            }
        }
    }

    suspend fun getById(id: Int): List<Exam> {
        return dao.getCourseWithExamsBy(id)?.toExams() ?: emptyList()
    }

    suspend fun add(exam: Exam) {
        dao.insert(ExamEntity.fromExam(exam))
    }

    suspend fun remove(exam: Exam) {
        dao.delete(ExamEntity.fromExam(exam))
    }
}