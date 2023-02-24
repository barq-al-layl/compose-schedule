package com.ba.schedule.domain.usecase.lectures

import com.ba.schedule.domain.model.Lecture
import com.ba.schedule.domain.repository.LecturesRepository
import com.ba.schedule.domain.usecase.UseCase
import javax.inject.Inject

class AddLectureUseCase @Inject constructor(
    private val repository: LecturesRepository,
) : UseCase<AddLectureParameter, Unit>() {
    override suspend fun execute(parameters: AddLectureParameter) {
        repository.add(parameters.lecture)
    }
}

data class AddLectureParameter(val lecture: Lecture)