package com.ba.schedule.domain.model

data class Exam(
    val course: Course? = null,
    val date: String,
    val time: String,
    val type: ExamType,
)
