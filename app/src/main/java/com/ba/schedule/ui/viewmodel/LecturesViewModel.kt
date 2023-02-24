package com.ba.schedule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.domain.model.Lecture
import com.ba.schedule.domain.usecase.lectures.*
import com.ba.schedule.domain.util.data
import com.ba.schedule.domain.model.SnackbarAction
import com.ba.schedule.domain.model.SnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LecturesViewModel @Inject constructor(
    getLecturesUseCase: GetLecturesUseCase,
    private val addLectureUseCase: AddLectureUseCase,
    private val removeLectureUseCase: RemoveLectureUseCase,
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

    private val _message = MutableSharedFlow<SnackbarMessage>()
    val message = _message.asSharedFlow()

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
            removedLecture = selectedLectures.value
            selectedLectures.value.forEach {
                removeLectureUseCase(RemoveLectureParameter(it))
            }
            _selectedLectures.update { emptyList() }
            _message.emit(
                SnackbarMessage(
                    message = "Lecture removed!",
                    action = SnackbarAction(
                        label = "Undo",
                        perform = {
                            viewModelScope.launch action@{
                                removedLecture.forEach {
                                    addLectureUseCase(AddLectureParameter(it))
                                }
                            }
                        }
                    ),
                )
            )
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