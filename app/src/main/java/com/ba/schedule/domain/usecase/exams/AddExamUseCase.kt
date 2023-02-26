package com.ba.schedule.domain.usecase.exams

import com.ba.schedule.domain.model.Exam
import com.ba.schedule.domain.repository.ExamsRepository
import com.ba.schedule.domain.usecase.UseCase
import javax.inject.Inject

class AddExamUseCase @Inject constructor(
    private val repository: ExamsRepository,
) : UseCase<AddExamUseCaseParameter, Unit>() {
    override suspend fun execute(parameters: AddExamUseCaseParameter) {
        repository.add(parameters.exam)
    }
}

data class AddExamUseCaseParameter(val exam: Exam)