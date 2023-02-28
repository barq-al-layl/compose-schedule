package com.ba.schedule.data.database

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class ScheduleDataStore @Inject constructor(
    @ApplicationContext private val context: Context,
) {

    val themeMode = context.dataStore.data.map {
        it[THEME_MODE]
    }

    val useDynamicColors = context.dataStore.data.map {
        it[USE_DYNAMIC_COLORS]
    }

    val totalLectures = context.dataStore.data.map {
        it[TOTAL_LECTURES]
    }

    val startTime = context.dataStore.data.map {
        it[START_TIME]
    }

    val lectureDuration = context.dataStore.data.map {
        it[LECTURE_DURATION]
    }

    suspend fun setThemeMode(mode: Int) = setValue(THEME_MODE, mode)
    suspend fun setUseDynamicColors(value: Boolean) = setValue(USE_DYNAMIC_COLORS, value)
    suspend fun setTotalLectures(total: Int) = setValue(TOTAL_LECTURES, total)
    suspend fun setStartTime(time: String) = setValue(START_TIME, time)
    suspend fun setLectureDuration(minutes: Int) = setValue(LECTURE_DURATION, minutes)

    private suspend fun <T> setValue(key: Preferences.Key<T>, value: T) {
        context.dataStore.edit { preferences ->
            preferences[key] = value
        }
    }

    companion object {
        private const val DataStoreName = "settings_datastore"
        private val Context.dataStore by preferencesDataStore(name = DataStoreName)

        private val THEME_MODE = intPreferencesKey("theme_mode")
        private val USE_DYNAMIC_COLORS = booleanPreferencesKey("dynamic_colors")
        private val TOTAL_LECTURES = intPreferencesKey("total_lectures")
        private val LECTURE_DURATION = intPreferencesKey("lecture_duration")
        private val START_TIME = stringPreferencesKey("start_time")
    }
}