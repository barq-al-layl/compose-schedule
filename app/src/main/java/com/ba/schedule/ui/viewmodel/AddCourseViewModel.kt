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
import com.ba.schedule.ui.util.AddCourseTextFieldEvent
import com.ba.schedule.ui.util.AddCourseTextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import javax.inject.Inject

@HiltViewModel
class AddCourseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCourseByIdUseCase: GetCourseByIdUseCase,
    private val addCourseUseCase: AddCourseUseCase,
) : ViewModel() {

    private val thisYear = LocalDate.now().year

    private val _courseName = MutableStateFlow("")
    val courseName = _courseName.asStateFlow()

    private val _final = MutableStateFlow(
        AddCourseTextFieldState(year = thisYear.toString(), minute = "00")
    )
    val final = _final.asStateFlow()

    private val _midterm = MutableStateFlow(
        AddCourseTextFieldState(year = thisYear.toString(), minute = "00")
    )
    val midterm = _midterm.asStateFlow()

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
                id = courseId.takeIf { it != -1 },
                name = name,
            )
            addCourseUseCase(AddCourseParameter(course))
        }
        return true
    }

    fun onNameChange(value: String) {
        _courseName.update { value }
    }

    fun onFinalChange(event: AddCourseTextFieldEvent) {
        event.updateFlow(_final)
    }

    fun onMidtermChange(event: AddCourseTextFieldEvent) {
        event.updateFlow(_midterm)
    }

    private fun AddCourseTextFieldEvent.updateFlow(
        flow: MutableStateFlow<AddCourseTextFieldState>,
    ) = flow.update {
        when (this) {
            is AddCourseTextFieldEvent.YearChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x !in thisYear..(thisYear + 10)) return
                if (value.length > 4) return
                it.copy(year = value)
            }
            is AddCourseTextFieldEvent.MonthChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x > 12 || value.length > 2) return
                if (x == 0 && value.length > 1) return
                it.copy(month = value)
            }
            is AddCourseTextFieldEvent.DayChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x > 31 || value.length > 2) return
                if (x == 0 && value.length > 1) return
                it.copy(day = value)
            }
            is AddCourseTextFieldEvent.HourChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x > 12 || value.length > 2) return
                if (x == 0 && value.length > 1) return
                it.copy(hour = value)
            }
            is AddCourseTextFieldEvent.MinuteChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x > 60 || value.length > 2) return
                it.copy(minute = value)
            }
            AddCourseTextFieldEvent.Reset -> AddCourseTextFieldState()
        }
    }
}
