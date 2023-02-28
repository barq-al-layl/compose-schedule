package com.ba.schedule.model

import androidx.annotation.StringRes

data class SnackbarAction(
    @StringRes val label: Int,
    val perform: () -> Unit,
)
