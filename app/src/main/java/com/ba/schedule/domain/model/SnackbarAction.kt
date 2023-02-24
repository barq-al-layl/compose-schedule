package com.ba.schedule.domain.model

data class SnackbarAction(
    val label: String,
    val perform: () -> Unit,
)
