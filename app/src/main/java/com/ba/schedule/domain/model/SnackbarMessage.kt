package com.ba.schedule.domain.model

import java.util.*

data class SnackbarMessage(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    val message: String,
    val dismissible: Boolean = true,
    val action: SnackbarAction? = null,
)
