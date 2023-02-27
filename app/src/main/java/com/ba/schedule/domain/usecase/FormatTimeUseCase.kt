package com.ba.schedule.domain.usecase

import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class FormatTimeUseCase @Inject constructor() : UseCase<FormatTimeUseCaseParameter, String>() {
    private val dateTimeFormatter = DateTimeFormatter.ofPattern("h:mm a")
    override suspend fun execute(parameters: FormatTimeUseCaseParameter): String {
        return parameters.localTime
            .format(dateTimeFormatter)
            .substringBefore(' ')
    }
}

data class FormatTimeUseCaseParameter(val localTime: LocalTime)