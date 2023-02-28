package com.ba.schedule.model

data class Lecture(
    val day: Int = -1,
    val time: Int = -1,
    val course: Course = Course(),
)
