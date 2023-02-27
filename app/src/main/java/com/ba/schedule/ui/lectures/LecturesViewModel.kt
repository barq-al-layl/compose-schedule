package com.ba.schedule.ui.lectures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.R
import com.ba.schedule.domain.model.Lecture
import com.ba.schedule.domain.model.SnackbarAction
import com.ba.schedule.domain.model.SnackbarManager
import com.ba.schedule.domain.model.SnackbarMessage
import com.ba.schedule.domain.usecase.lectures.*
import com.ba.schedule.domain.util.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LecturesViewModel @Inject constructor(
    getLecturesUseCase: GetLecturesUseCase,
    private val addLectureUseCase: AddLectureUseCase,
    private val removeLectureUseCase: RemoveLectureUseCase,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {

    private val _isLayoutLocked = MutableStateFlow(true)
    val isLayoutLocked = _isLayoutLocked.asStateFlow()

    val lectures = getLecturesUseCase(Unit)
        .mapNotNull { it.data }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    private val _selectedLectures = MutableStateFlow(listOf<Lecture>())
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
                removeLectureUseCase(RemoveLectureParameter(it))
            }
            _selectedLectures.update { emptyList() }
            val message = SnackbarMessage(
                message = R.string.lectures_removed,
                action = SnackbarAction(
                    label = R.string.undo,
                    perform = {
                        viewModelScope.launch action@{
                            removedLecture.forEach {
                                addLectureUseCase(AddLectureParameter(it))
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