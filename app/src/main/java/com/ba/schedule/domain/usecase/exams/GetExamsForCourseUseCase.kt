package com.ba.schedule.domain.usecase.exams

import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.model.Exam
import com.ba.schedule.domain.repository.ExamsRepository
import com.ba.schedule.domain.usecase.UseCase
import javax.inject.Inject

class GetExamsForCourseUseCase @Inject constructor(
    private val repository: ExamsRepository,
) : UseCase<GetExamsForCourseUseCaseParameter, List<Exam>>() {
    override suspend fun execute(parameters: GetExamsForCourseUseCaseParameter): List<Exam> {
        return repository.getById(parameters.course.id!!)
    }
}

data class GetExamsForCourseUseCaseParameter(val course: Course)