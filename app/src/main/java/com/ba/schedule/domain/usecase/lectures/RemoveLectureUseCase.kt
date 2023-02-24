package com.ba.schedule.domain.usecase.lectures

import com.ba.schedule.domain.model.Lecture
import com.ba.schedule.domain.repository.LecturesRepository
import com.ba.schedule.domain.usecase.UseCase
import javax.inject.Inject

class RemoveLectureUseCase @Inject constructor(
    private val repository: LecturesRepository,
) : UseCase<RemoveLectureParameter, Unit>() {
    override suspend fun execute(parameters: RemoveLectureParameter) {
        repository.remove(parameters.lecture)
    }
}

data class RemoveLectureParameter(val lecture: Lecture)