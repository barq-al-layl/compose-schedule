package com.ba.schedule.data.repository

import android.annotation.SuppressLint
import android.os.Build
import com.ba.schedule.data.database.ScheduleDataStore
import com.ba.schedule.model.ThemeMode
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

class SettingsRepository @Inject constructor(
    private val dataStore: ScheduleDataStore,
) {
    @SuppressLint("ObsoleteSdkInt")
    val supportDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    fun getThemeModeStream(): Flow<ThemeMode> {
        return dataStore.themeMode.map { ThemeMode.values()[it ?: 0] }
    }

    suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.setThemeMode(mode.ordinal)
    }

    fun getUseDynamicColorStream(): Flow<Boolean> {
        return dataStore.useDynamicColors.map { it ?: supportDynamicColor }
    }

    suspend fun setUseDynamicColor(value: Boolean) {
        dataStore.setUseDynamicColors(value)
    }

    fun getTotalLecturesStream(): Flow<Int> {
        return dataStore.totalLectures.map { it ?: 5 }
    }

    suspend fun setTotalLectures(total: Int) {
        dataStore.setTotalLectures(total)
    }

    fun getLectureDurationStream(): Flow<Duration> {
        return dataStore.lectureDuration.map { (it ?: 120).minutes }
    }

    suspend fun setLectureDuration(duration: Duration) {
        dataStore.setLectureDuration(duration.toInt(DurationUnit.MINUTES))
    }

    fun getStartTimeStream(): Flow<LocalTime> {
        return dataStore.startTime.map {
            it?.let { LocalTime.parse(it) } ?: LocalTime.of(8, 0)
        }
    }

    suspend fun setStartTime(localTime: LocalTime) {
        dataStore.setStartTime(localTime.toString())
    }
}