package com.ba.schedule.model

import java.time.LocalDate
import java.time.LocalTime

data class Exam(
    val course: Course? = null,
    val date: LocalDate,
    val time: LocalTime,
    val type: ExamType,
)
