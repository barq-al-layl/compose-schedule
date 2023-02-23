package com.ba.schedule.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.usecase.courses.AddCourseParameter
import com.ba.schedule.domain.usecase.courses.AddCourseUseCase
import com.ba.schedule.domain.usecase.courses.GetCourseByIdParameter
import com.ba.schedule.domain.usecase.courses.GetCourseByIdUseCase
import com.ba.schedule.domain.util.data
import com.ba.schedule.ui.navigation.MainDestination
import com.ba.schedule.ui.util.SnackbarMessage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AddCourseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCourseByIdUseCase: GetCourseByIdUseCase,
    private val addCourseUseCase: AddCourseUseCase,
) : ViewModel() {

    private val _courseName = MutableStateFlow("")
    val courseName = _courseName.asStateFlow()

    private val _final = MutableStateFlow("")
    val final = _final.asStateFlow()

    private val _midterm = MutableStateFlow("")
    val midterm = _midterm.asStateFlow()

    private val _message = MutableSharedFlow<SnackbarMessage>()
    val message = _message.asSharedFlow()

    private val courseId: Int = savedStateHandle[MainDestination.kCourseId]!!

    init {
        viewModelScope.launch {
            getCourseByIdUseCase(GetCourseByIdParameter(courseId)).data?.let { course ->
                _courseName.update { course.name }
            }
        }
    }

    fun onAddCourse(): Boolean {
        val name = courseName.value.trim()
        if (name.isEmpty()) return false
        viewModelScope.launch {
            val course = Course(
                id = courseId,
                name = name,
            )
            addCourseUseCase(AddCourseParameter(course))
        }
        return true
    }

    fun onNameChange(value: String) {
        _courseName.update { value }
    }

    fun onFinalChange(value: String) {
        _final.update { value }
    }

    fun onMidtermChange(value: String) {
        _midterm.update { value }
    }
}