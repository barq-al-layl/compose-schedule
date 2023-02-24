package com.ba.schedule.domain.usecase.courses

import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.repository.CoursesRepository
import com.ba.schedule.domain.usecase.UseCase
import javax.inject.Inject

class AddCourseUseCase @Inject constructor(
    private val repository: CoursesRepository,
) : UseCase<AddCourseParameter, Unit>() {
    override suspend fun execute(parameters: AddCourseParameter) {
        repository.add(parameters.course)
    }
}

data class AddCourseParameter(val course: Course)