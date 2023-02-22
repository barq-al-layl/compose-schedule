package com.ba.schedule.domain.usecase.courses

import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.repository.CoursesRepository
import com.ba.schedule.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class DeleteCourseUseCase @Inject constructor(
    coroutineDispatcher: CoroutineDispatcher,
    private val repository: CoursesRepository,
) : UseCase<DeleteCourseParameter, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameters: DeleteCourseParameter) {
        repository.delete(parameters.course)
    }
}

data class DeleteCourseParameter(val course: Course)