package com.ba.schedule.domain.usecase.exams

import com.ba.schedule.domain.usecase.UseCase
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FormatDateUseCase @Inject constructor() : UseCase<FormatDateUseCaseParameter, String>() {
    private val dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd\nEEEE")
    override suspend fun execute(parameters: FormatDateUseCaseParameter): String {
        return parameters.localDate.format(dateFormatter)
    }
}

data class FormatDateUseCaseParameter(val localDate: LocalDate)