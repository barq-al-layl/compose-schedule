package com.ba.schedule.domain.usecase.exams

import com.ba.schedule.domain.model.Exam
import com.ba.schedule.domain.repository.ExamsRepository
import com.ba.schedule.domain.usecase.UseCase
import javax.inject.Inject

class RemoveExamUseCase @Inject constructor(
    private val repository: ExamsRepository,
) : UseCase<RemoveExamUseCaseParameter, Unit>() {
    override suspend fun execute(parameters: RemoveExamUseCaseParameter) {
        repository.remove(parameters.exam)
    }
}

data class RemoveExamUseCaseParameter(val exam: Exam)