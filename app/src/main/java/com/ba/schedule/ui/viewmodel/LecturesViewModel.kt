package com.ba.schedule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.domain.model.Lecture
import com.ba.schedule.ui.util.SnackbarAction
import com.ba.schedule.ui.util.SnackbarMessage
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

    private var removedLecture: Lecture? = null

    fun onLayoutLockChange() {
        _isLayoutLocked.update { !it }
    }

    fun onRemoveLecture(lecture: Lecture) {
        viewModelScope.launch {
            removedLecture = lecture
            removeLectureUseCase(RemoveLectureParameter(lecture))
            _message.emit(
                SnackbarMessage(
                    message = "Lecture removed!",
                    action = SnackbarAction(
                        label = "Undo",
                        perform = {
                            viewModelScope.launch action@{
                                addLectureUseCase(
                                    AddLectureParameter(removedLecture ?: return@action)
                                )
                                removedLecture = null
                            }
                        }
                    ),
                )
            )
        }
    }
}