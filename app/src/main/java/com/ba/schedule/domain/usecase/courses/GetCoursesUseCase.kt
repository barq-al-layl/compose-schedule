package com.ba.schedule.domain.usecase.courses

import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.repository.CoursesRepository
import com.ba.schedule.domain.usecase.FlowUseCase
import com.ba.schedule.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetCoursesUseCase @Inject constructor(
    coroutineDispatcher: CoroutineDispatcher,
    private val repository: CoursesRepository,
) : FlowUseCase<Unit, List<Course>>(coroutineDispatcher) {
    override fun execute(parameters: Unit): Flow<Resource<List<Course>>> {
        return repository.getAll().map { courses ->
            Resource.Success(
                courses.sortedWith(
                    compareBy({ it.name.length }, { it.name }),
                )
            )
        }
    }
}