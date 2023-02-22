package com.ba.schedule.domain.usecase.courses

import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.repository.CoursesRepository
import com.ba.schedule.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class AddCourseUseCase @Inject constructor(
    coroutineDispatcher: CoroutineDispatcher,
    private val repository: CoursesRepository,
) : UseCase<AddCourseParameter, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameters: AddCourseParameter) {
        repository.add(parameters.course)
    }
}

data class AddCourseParameter(val course: Course)