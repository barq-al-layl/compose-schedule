package com.ba.schedule.domain.usecase.courses

import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.repository.CoursesRepository
import com.ba.schedule.domain.usecase.UseCase
import javax.inject.Inject

class EditCourseUseCase @Inject constructor(
    private val repository: CoursesRepository,
) : UseCase<AddCourseParameter, Long>() {
    override suspend fun execute(parameters: AddCourseParameter): Long {
        return repository.add(parameters.course)
    }
}

data class AddCourseParameter(val course: Course)