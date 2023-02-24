package com.ba.schedule.domain.usecase.courses

import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.repository.CoursesRepository
import com.ba.schedule.domain.usecase.UseCase
import javax.inject.Inject

class GetCourseByIdUseCase @Inject constructor(
    private val repository: CoursesRepository,
) : UseCase<GetCourseByIdParameter, Course?>() {
    override suspend fun execute(parameters: GetCourseByIdParameter): Course? {
        return repository.getById(parameters.id)
    }
}

data class GetCourseByIdParameter(val id: Int)