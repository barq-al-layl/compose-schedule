package com.ba.schedule.ui.feature.lectures

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.model.Lecture
import com.ba.schedule.domain.repository.LecturesRepository
import com.ba.schedule.ui.util.SnackbarAction
import com.ba.schedule.ui.util.SnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LecturesViewModel @Inject constructor(
    private val repository: LecturesRepository,
) : ViewModel() {

    private val _isLayoutLocked = MutableStateFlow(true)
    val isLayoutLocked = _isLayoutLocked.asStateFlow()

    val lectures = repository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList(),
    )

    private val _message = MutableSharedFlow<SnackbarMessage>()
    val message = _message.asSharedFlow()

    private var removedLecture: Lecture? = null

    private var selectedLecture: Lecture? = null

    fun onLayoutLockChange() {
        _isLayoutLocked.update { !it }
    }

    fun onLectureClick(day: Int, time: Int) {
        selectedLecture = Lecture(day = day, time = time)
    }

    fun onAddLecture(course: Course) {
        viewModelScope.launch {
            repository.add(selectedLecture?.copy(course = course) ?: return@launch)
            selectedLecture = null
        }
    }

    fun onRemoveLecture(lecture: Lecture) {
        viewModelScope.launch {
            removedLecture = lecture
            repository.remove(lecture)
            _message.emit(
                SnackbarMessage(
                    message = "Lecture removed!",
                    action = SnackbarAction(
                        label = "Undo",
                        perform = {
                            viewModelScope.launch action@{
                                repository.add(removedLecture ?: return@action)
                                removedLecture = null
                            }
                        }
                    ),
                )
            )
        }
    }
}