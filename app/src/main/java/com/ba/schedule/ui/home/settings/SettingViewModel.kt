package com.ba.schedule.ui.home.settings

import android.annotation.SuppressLint
import android.os.Build
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.model.ThemeMode
import com.ba.schedule.data.repository.SettingsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.time.LocalTime
import javax.inject.Inject
import kotlin.time.Duration.Companion.minutes

@HiltViewModel
class SettingViewModel @Inject constructor(
    private val repository: SettingsRepository,
) : ViewModel() {
    @SuppressLint("ObsoleteSdkInt")
    val supportDynamicColor = Build.VERSION.SDK_INT >= Build.VERSION_CODES.S

    val themeMode = repository.getThemeModeStream().toStateFlow(ThemeMode.Dark)
    val useDynamicColors = repository.getUseDynamicColorStream()
        .map { it && supportDynamicColor }.toStateFlow(false)
    val totalLectures = repository.getTotalLecturesStream().toStateFlow(5)
    val startTime = repository.getStartTimeStream().toStateFlow(LocalTime.of(8, 0))
    val lectureDuration = repository.getLectureDurationStream().map {
        it.inWholeMinutes.toString()
    }.toStateFlow("120")

    fun onThemeModeChanged(mode: ThemeMode) {
        viewModelScope.launch {
            repository.setThemeMode(mode)
        }
    }

    fun onUseDynamicColorChanged(value: Boolean) {
        viewModelScope.launch {
            repository.setUseDynamicColor(value && supportDynamicColor)
        }
    }

    fun onStartTimeChanged(hour: Int, minute: Int) {
        viewModelScope.launch {
            val localTime = LocalTime.of(hour, minute)
            repository.setStartTime(localTime)
        }
    }

    fun onLecturesPerDayChanged(value: Int) {
        viewModelScope.launch {
            repository.setTotalLectures(value)
        }
    }

    fun onLectureDurationChanged(value: String): Boolean {
        if (value.isEmpty()) return false
        viewModelScope.launch {
            repository.setLectureDuration(value.toInt().minutes)
        }
        return true
    }

    private fun <T> Flow<T>.toStateFlow(initialValue: T) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialValue,
    )
}
