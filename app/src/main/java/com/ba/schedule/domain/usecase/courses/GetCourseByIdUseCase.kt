package com.ba.schedule.domain.usecase.courses

import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.repository.CoursesRepository
import com.ba.schedule.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class GetCourseByIdUseCase @Inject constructor(
    coroutineDispatcher: CoroutineDispatcher,
    private val repository: CoursesRepository,
) : UseCase<GetCourseByIdParameter, Course?>(coroutineDispatcher) {
    override suspend fun execute(parameters: GetCourseByIdParameter): Course? {
        return repository.getById(parameters.id)
    }
}

data class GetCourseByIdParameter(val id: Int)