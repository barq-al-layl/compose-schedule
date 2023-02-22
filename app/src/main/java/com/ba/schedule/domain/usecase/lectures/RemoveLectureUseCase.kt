package com.ba.schedule.domain.usecase.lectures

import com.ba.schedule.domain.model.Lecture
import com.ba.schedule.domain.repository.LecturesRepository
import com.ba.schedule.domain.usecase.UseCase
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class RemoveLectureUseCase @Inject constructor(
    coroutineDispatcher: CoroutineDispatcher,
    private val repository: LecturesRepository,
) : UseCase<RemoveLectureParameter, Unit>(coroutineDispatcher) {
    override suspend fun execute(parameters: RemoveLectureParameter) {
        repository.remove(parameters.lecture)
    }
}

data class RemoveLectureParameter(val lecture: Lecture)