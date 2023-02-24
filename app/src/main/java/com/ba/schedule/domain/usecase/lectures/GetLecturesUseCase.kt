package com.ba.schedule.domain.usecase.lectures

import com.ba.schedule.domain.model.Lecture
import com.ba.schedule.domain.repository.LecturesRepository
import com.ba.schedule.domain.usecase.FlowUseCase
import com.ba.schedule.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class GetLecturesUseCase @Inject constructor(
    private val repository: LecturesRepository,
) : FlowUseCase<Unit, List<Lecture>>() {
    override fun execute(parameters: Unit): Flow<Resource<List<Lecture>>> {
        return repository.getAll().map { Resource.Success(it) }
    }
}