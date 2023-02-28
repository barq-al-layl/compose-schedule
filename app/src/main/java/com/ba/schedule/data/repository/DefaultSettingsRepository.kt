package com.ba.schedule.data.repository

import com.ba.schedule.data.database.ScheduleDataStore
import com.ba.schedule.domain.model.ThemeMode
import com.ba.schedule.domain.repository.SettingsRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalTime
import javax.inject.Inject
import kotlin.time.Duration
import kotlin.time.Duration.Companion.minutes
import kotlin.time.DurationUnit

class DefaultSettingsRepository @Inject constructor(
    private val dataStore: ScheduleDataStore,
) : SettingsRepository {
    override fun getThemeModeStream(): Flow<ThemeMode> {
        return dataStore.themeMode.map { ThemeMode.values()[it ?: 0] }
    }

    override suspend fun setThemeMode(mode: ThemeMode) {
        dataStore.setThemeMode(mode.ordinal)
    }

    override fun getUseDynamicColorStream(): Flow<Boolean> {
        return dataStore.useDynamicColors.map { it ?: false }
    }

    override suspend fun setUseDynamicColor(value: Boolean) {
        dataStore.setUseDynamicColors(value)
    }

    override fun getTotalLecturesStream(): Flow<Int> {
        return dataStore.totalLectures.map { it ?: 5 }
    }

    override suspend fun setTotalLectures(total: Int) {
        dataStore.setTotalLectures(total)
    }

    override fun getLectureDurationStream(): Flow<Duration> {
        return dataStore.lectureDuration.map { (it ?: 120).minutes }
    }

    override suspend fun setLectureDuration(duration: Duration) {
        dataStore.setLectureDuration(duration.toInt(DurationUnit.MINUTES))
    }

    override fun getStartTimeStream(): Flow<LocalTime> {
        return dataStore.startTime.map {
            it?.let { LocalTime.parse(it) } ?: LocalTime.of(8, 0)
        }
    }

    override suspend fun setStartTime(localTime: LocalTime) {
        dataStore.setStartTime(localTime.toString())
    }
}