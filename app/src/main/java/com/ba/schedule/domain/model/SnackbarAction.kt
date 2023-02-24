package com.ba.schedule.domain.model

import androidx.annotation.StringRes

data class SnackbarAction(
    @StringRes val label: Int,
    val perform: () -> Unit,
)
