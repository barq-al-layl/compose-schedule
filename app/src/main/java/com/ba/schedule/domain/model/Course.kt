package com.ba.schedule.domain.model

import java.time.LocalDateTime

data class Course(
    val id: Int? = null,
    val name: String = "",
    val final: LocalDateTime? = null,
    val midterm: LocalDateTime? = null,
)
