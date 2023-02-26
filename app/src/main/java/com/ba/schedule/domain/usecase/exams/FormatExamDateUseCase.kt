package com.ba.schedule.domain.usecase.exams

import com.ba.schedule.domain.model.Exam
import com.ba.schedule.domain.usecase.UseCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FormatExamDateUseCase @Inject constructor() : UseCase<FormatExamDateUseCaseParameter, Exam>() {
    private val dateFormatter = DateTimeFormatter.ofPattern("uuuu/MM/dd\nEEEE")
    override suspend fun execute(parameters: FormatExamDateUseCaseParameter): Exam {
        val date = LocalDate.parse(parameters.exam.date)
        return parameters.exam.copy(date = dateFormatter.format(date))
    }
}

data class FormatExamDateUseCaseParameter(val exam: Exam)