package com.ba.schedule.ui.home.courses

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.R
import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.model.SnackbarAction
import com.ba.schedule.domain.model.SnackbarManager
import com.ba.schedule.domain.model.SnackbarMessage
import com.ba.schedule.domain.usecase.courses.*
import com.ba.schedule.domain.util.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CoursesViewModel @Inject constructor(
    getCoursesUseCase: GetCoursesUseCase,
    private val editCourseUseCase: EditCourseUseCase,
    private val deleteCourseUseCase: DeleteCourseUseCase,
    private val snackbarManager: SnackbarManager,
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

    private val deletedCourse = mutableListOf<Course>()

    init {
        showSearch.onEach { visible ->
            if (!visible) {
                _searchString.update { "" }
            }
        }.launchIn(viewModelScope)
    }

    fun onDeleteCourse(course: Course) {
        viewModelScope.launch {
            deletedCourse += course
            deleteCourseUseCase(DeleteCourseParameter(course))
            val message = SnackbarMessage(
                message = R.string.course_deleted,
                action = SnackbarAction(R.string.undo) {
                    viewModelScope.launch {
                        editCourseUseCase(
                            AddCourseParameter(deletedCourse.removeFirst())
                        )
                    }
                },
            )
            snackbarManager.showMessage(message)
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