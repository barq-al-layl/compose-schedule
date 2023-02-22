package com.ba.schedule.ui.feature.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.repository.CoursesRepository
import com.ba.schedule.ui.util.SnackbarAction
import com.ba.schedule.ui.util.SnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    private val repository: CoursesRepository,
) : ViewModel() {

    private val _showSearch = MutableStateFlow(false)
    val showSearch = _showSearch.asStateFlow()

    private val _searchString = MutableStateFlow("")
    val searchString = _searchString.asStateFlow()

    private val _expandedItem = MutableStateFlow(-1)
    val expandedItem = _expandedItem.asStateFlow()

    val courses = repository.getAll().onEach {
        _expandedItem.update { -1 }
    }.combine(searchString) { courses, substring ->
        courses.filter { it.name.contains(substring, ignoreCase = true) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.Eagerly,
        initialValue = emptyList(),
    )

    private val _showDialog = MutableStateFlow(false)
    val showDialog = _showDialog.asStateFlow()

    private val _dialogValue = MutableStateFlow("")
    val dialogValue = _dialogValue.asStateFlow()

    private val selectedCourse = MutableStateFlow<Course?>(null)

    private val _message = MutableSharedFlow<SnackbarMessage>()
    val message = _message.asSharedFlow()

    private var lastDeletedCourse: Course? = null

    init {
        showSearch.onEach { visible ->
            if (!visible) {
                _searchString.update { "" }
            }
        }.launchIn(viewModelScope)

        showDialog.onEach { visible ->
            if (!visible) {
                selectedCourse.update { null }
            }
        }.launchIn(viewModelScope)

        selectedCourse.onEach { course ->
            _dialogValue.update { course?.name ?: "" }
        }.launchIn(viewModelScope)
    }

    fun onAddCourse() {
        viewModelScope.launch {
            val courseName = dialogValue.value.trim()
            val course = Course(
                id = selectedCourse.value?.id,
                name = courseName,
            )
            repository.add(course)
            _dialogValue.update { "" }
            onShowDialogChange()
        }
    }

    fun onDeleteCourse(course: Course) {
        viewModelScope.launch {
            lastDeletedCourse = course
            repository.delete(course)
            _message.emit(
                SnackbarMessage(
                    message = "Course deleted!",
                    action = SnackbarAction("Restore") {
                        viewModelScope.launch action@{
                            repository.add(lastDeletedCourse ?: return@action)
                            lastDeletedCourse = null
                        }
                    },
                ),
            )
        }
    }

    fun onExpandItem(id: Int) {
        _expandedItem.update {
            if (it == id) -1 else id
        }
    }

    fun onDialogValueChange(value: String) {
        _dialogValue.update { value }
    }

    fun onEditCourse(course: Course) {
        selectedCourse.update { course }
        _dialogValue.update { course.name }
        onShowDialogChange()
    }

    fun onShowDialogChange() {
        _showDialog.update { !it }
    }

    fun onSearchValueChange(value: String) {
        _searchString.update { value }
    }

    fun onShowSearchChange() {
        _showSearch.update { !it }
    }
}