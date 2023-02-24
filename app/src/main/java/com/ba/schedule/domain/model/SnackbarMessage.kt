package com.ba.schedule.domain.model

data class SnackbarMessage(
    val message: String,
    val dismissible: Boolean = true,
    val action: SnackbarAction? = null,
)
