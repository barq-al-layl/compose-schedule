package com.ba.schedule.domain.model

data class Event(
    val date: String,
    val time: List<String> = emptyList(),
    val courses: List<Course> = emptyList(),
)
