package com.ba.schedule.domain.repository

import com.ba.schedule.domain.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import java.time.LocalTime
import kotlin.time.Duration

interface SettingsRepository {
    fun getThemeModeStream(): Flow<ThemeMode>
    suspend fun setThemeMode(mode: ThemeMode)
    fun getUseDynamicColorStream(): Flow<Boolean>
    suspend fun setUseDynamicColor(value: Boolean)
    fun getTotalLecturesStream(): Flow<Int>
    suspend fun setTotalLectures(total: Int)
    fun getLectureDurationStream(): Flow<Duration>
    suspend fun setLectureDuration(duration: Duration)
    fun getStartTimeStream(): Flow<LocalTime>
    suspend fun setStartTime(localTime: LocalTime)
}