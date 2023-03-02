package com.ba.schedule.ui.home.lectures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.R
import com.ba.schedule.data.repository.LecturesRepository
import com.ba.schedule.data.repository.SettingsRepository
import com.ba.schedule.model.*
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import java.time.DayOfWeek
import java.time.format.DateTimeFormatter
import java.time.format.TextStyle
import java.util.*
import javax.inject.Inject
import kotlin.time.toJavaDuration

@HiltViewModel
class LecturesViewModel @Inject constructor(
    settingsRepository: SettingsRepository,
    private val lecturesRepository: LecturesRepository,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm")
    val weekDays = listOf(
        DayOfWeek.SATURDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        DayOfWeek.SUNDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        DayOfWeek.MONDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        DayOfWeek.TUESDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        DayOfWeek.WEDNESDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
        DayOfWeek.THURSDAY.getDisplayName(TextStyle.FULL, Locale.getDefault()),
    )
    val lectureTime = combine(
        settingsRepository.getStartTimeStream(),
        settingsRepository.getLectureDurationStream(),
        settingsRepository.getTotalLecturesStream(),
    ) { localTime, duration, total ->
        val javaDuration = duration.toJavaDuration()
        var startTime = localTime
        var endTime = startTime + javaDuration
        val res = mutableListOf<LectureTime>()
        for (i in 0 until total) {
            res += LectureTime(
                start = timeFormatter.format(startTime),
                end = timeFormatter.format(endTime),
            )
            startTime = endTime
            endTime += javaDuration
        }
        res.toList()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    private val _isLayoutLocked = MutableStateFlow(true)
    val isLayoutLocked = _isLayoutLocked.asStateFlow()

    val lectures = lecturesRepository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    private val _selectedLectures = MutableStateFlow(emptyList<Lecture>())
    val selectedLectures = _selectedLectures.asStateFlow()

    val isRemoveVisible = selectedLectures.map { it.isNotEmpty() }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = false,
    )

    private var removedLecture = emptyList<Lecture>()

    init {
        isLayoutLocked.onEach {
            if (it) _selectedLectures.update { emptyList() }
        }.launchIn(viewModelScope)
    }

    fun onRemoveLecture() {
        viewModelScope.launch {
            removedLecture = selectedLectures.value.sortedWith(
                compareBy(Lecture::day, Lecture::time)
            )
            removedLecture.forEach {
                lecturesRepository.remove(it)
            }
            _selectedLectures.update { emptyList() }
            val message = SnackbarMessage(
                message = R.string.lectures_removed,
                action = SnackbarAction(
                    label = R.string.undo,
                    perform = {
                        viewModelScope.launch action@{
                            removedLecture.forEach {
                                lecturesRepository.add(it)
                            }
                        }
                    }
                ),
            )
            snackbarManager.showMessage(message)
        }
    }

    fun onSelectLecture(lecture: Lecture?) {
        _selectedLectures.update {
            if (it.contains(lecture ?: return))
                it - lecture
            else
                it + lecture
        }
    }

    fun onLayoutLockChange() {
        _isLayoutLocked.update { !it }
    }
}