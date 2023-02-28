package com.ba.schedule.model

import androidx.annotation.StringRes
import java.util.*

data class SnackbarMessage(
    val id: Long = UUID.randomUUID().mostSignificantBits,
    @StringRes val message: Int,
    val dismissible: Boolean = true,
    val action: SnackbarAction? = null,
)
