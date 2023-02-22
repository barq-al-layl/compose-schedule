package com.ba.schedule.ui.util

data class SnackbarMessage(
    val message: String,
    val dismissible: Boolean = true,
    val action: SnackbarAction? = null,
)
