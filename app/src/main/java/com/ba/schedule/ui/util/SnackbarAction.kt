package com.ba.schedule.ui.util

data class SnackbarAction(
    val label: String,
    val perform: () -> Unit,
)
