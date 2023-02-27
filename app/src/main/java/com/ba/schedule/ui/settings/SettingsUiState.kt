package com.ba.schedule.ui.settings

import kotlin.time.Duration
import kotlin.time.Duration.Companion.hours

data class SettingsUiState(
    val isDarkTheme: Boolean = true,
    val useDynamicColor: Boolean = true,
    val startOfDay: Int = 8,
    val totalLectures: Int = 5,
    val lectureDuration: Duration = 2.hours,
)
