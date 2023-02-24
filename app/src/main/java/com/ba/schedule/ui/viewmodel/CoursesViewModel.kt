package com.ba.schedule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.usecase.courses.*
import com.ba.schedule.domain.util.data
import com.ba.schedule.ui.util.SnackbarAction
import com.ba.schedule.ui.util.SnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    getCoursesUseCase: GetCoursesUseCase,
    private val addCourseUseCase: AddCourseUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase,
) : ViewModel() {

    private val _showSearch = MutableStateFlow(false)
    val showSearch = _showSearch.asStateFlow()

    private val _searchString = MutableStateFlow("")
    val searchString = _searchString.asStateFlow()

    private val _expandedItem = MutableStateFlow(-1)
    val expandedItem = _expandedItem.asStateFlow()

    val courses = getCoursesUseCase(Unit)
        .mapNotNull { it.data }
        .onEach {
            _expandedItem.update { -1 }
        }.combine(searchString) { courses, substring ->
            courses.filter { it.name.contains(substring, ignoreCase = true) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    private val _message = MutableSharedFlow<SnackbarMessage>()
    val message = _message.asSharedFlow()

    private var lastDeletedCourse: Course? = null

    init {
        showSearch.onEach { visible ->
            if (!visible) {
                _searchString.update { "" }
            }
        }.launchIn(viewModelScope)
    }

    fun onDeleteCourse(course: Course) {
        viewModelScope.launch {
            lastDeletedCourse = course
            deleteCourseUseCase(DeleteCourseParameter(course))
            _message.emit(
                SnackbarMessage(
                    message = "Course deleted!",
                    action = SnackbarAction("Restore") {
                        viewModelScope.launch action@{
                            addCourseUseCase(
                                AddCourseParameter(lastDeletedCourse ?: return@action)
                            )
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

    fun onSearchValueChange(value: String) {
        _searchString.update { value }
    }

    fun onShowSearchChange() {
        _showSearch.update { !it }
    }
}