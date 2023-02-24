package com.ba.schedule.domain.usecase.courses

import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.repository.CoursesRepository
import com.ba.schedule.domain.usecase.UseCase
import javax.inject.Inject

class DeleteCourseUseCase @Inject constructor(
    private val repository: CoursesRepository,
) : UseCase<DeleteCourseParameter, Unit>() {
    override suspend fun execute(parameters: DeleteCourseParameter) {
        repository.delete(parameters.course)
    }
}

data class DeleteCourseParameter(val course: Course)