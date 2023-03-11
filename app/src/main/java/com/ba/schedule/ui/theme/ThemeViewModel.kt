package com.ba.schedule.ui.theme

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.data.repository.SettingsRepository
import com.ba.schedule.model.ThemeMode
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import javax.inject.Inject

@HiltViewModel
class ThemeViewModel @Inject constructor(
    repository: SettingsRepository,
) : ViewModel() {

    val themeMode = repository.getThemeModeStream().toStateFlow(ThemeMode.System)
    val useDynamicColors = repository.getUseDynamicColorStream()
        .toStateFlow(repository.supportDynamicColor)

    private fun <T> Flow<T>.toStateFlow(initialValue: T) = stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialValue,
    )
}