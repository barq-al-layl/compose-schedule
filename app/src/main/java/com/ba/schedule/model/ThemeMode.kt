package com.ba.schedule.model

import androidx.annotation.StringRes
import com.ba.schedule.R

enum class ThemeMode(@StringRes val label: Int) {
    System(R.string.system),
    Light(R.string.light),
    Dark(R.string.dark),
}