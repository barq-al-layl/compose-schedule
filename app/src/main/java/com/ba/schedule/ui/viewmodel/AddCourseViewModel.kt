package com.ba.schedule.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.domain.model.*
import com.ba.schedule.domain.usecase.courses.AddCourseParameter
import com.ba.schedule.domain.usecase.courses.AddCourseUseCase
import com.ba.schedule.domain.usecase.courses.GetCourseByIdParameter
import com.ba.schedule.domain.usecase.courses.GetCourseByIdUseCase
import com.ba.schedule.domain.usecase.exams.*
import com.ba.schedule.domain.util.data
import com.ba.schedule.ui.navigation.MainDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class AddCourseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCourseByIdUseCase: GetCourseByIdUseCase,
    private val addCourseUseCase: AddCourseUseCase,
    private val getExamsForCourseUseCase: GetExamsForCourseUseCase,
    private val addExamUseCase: AddExamUseCase,
    private val removeExamUseCase: RemoveExamUseCase,
) : ViewModel() {

    private val _courseName = MutableStateFlow("")
    val courseName = _courseName.asStateFlow()

    private val _final = MutableStateFlow(AddCourseTextFieldState())
    val final = _final.asStateFlow()

    private val _midterm = MutableStateFlow(AddCourseTextFieldState())
    val midterm = _midterm.asStateFlow()

    private val courseId: Int = savedStateHandle[MainDestination.kCourseId]!!

    private var finalExam: Exam? = null
    private var midtermExam: Exam? = null

    init {
        viewModelScope.launch {
            val course = getCourseByIdUseCase(GetCourseByIdParameter(courseId))
                .data ?: return@launch
            _courseName.update { course.name }
            val exams = getExamsForCourseUseCase(GetExamsForCourseUseCaseParameter(course))
                .data ?: return@launch
            exams.find { it.type == ExamType.Final }?.let {
                finalExam = it
                LocalDateTime.of(it.date, it.time).updateFlow(_final)
            }
            exams.find { it.type == ExamType.Midterm }?.let {
                midtermExam = it
                LocalDateTime.of(it.date, it.time).updateFlow(_midterm)
            }
        }
    }

    fun onAddCourse(): Boolean {
        val name = courseName.value.trim()
        if (name.isEmpty()) return false
        viewModelScope.launch {
            var course = Course(
                id = courseId.takeIf { it != -1 },
                name = name,
            )
            val courseId =
                addCourseUseCase(AddCourseParameter(course)).data?.toInt() ?: return@launch
            course = course.copy(id = courseId)
            if (final.value.isEmpty()) {
                finalExam?.let { removeExamUseCase(RemoveExamUseCaseParameter(it)) }
            } else {
                val exam = Exam(
                    course = course,
                    date = final.value.toLocalDate(),
                    time = final.value.toTimeString(),
                    type = ExamType.Final,
                )
                addExamUseCase(AddExamUseCaseParameter(exam))
            }
            if (midterm.value.isEmpty()) {
                midtermExam?.let { removeExamUseCase(RemoveExamUseCaseParameter(it)) }
            } else {
                val exam = Exam(
                    course = course,
                    date = midterm.value.toLocalDate(),
                    time = midterm.value.toTimeString(),
                    type = ExamType.Midterm,
                )
                addExamUseCase(AddExamUseCaseParameter(exam))
            }
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
                if (x !in 1..2300 && value.isNotEmpty()) return
                it.copy(year = value)
            }
            is AddCourseTextFieldEvent.MonthChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x !in 1..12 && value.isNotEmpty()) return
                it.copy(month = value)
            }
            is AddCourseTextFieldEvent.DayChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x !in 1..31 && value.isNotEmpty()) return
                it.copy(day = value)
            }
            is AddCourseTextFieldEvent.HourChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x !in 1..12 && value.isNotEmpty()) return
                it.copy(hour = value)
            }
            is AddCourseTextFieldEvent.MinuteChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x !in 1..59 && value.length > 2) return
                it.copy(minute = value)
            }
            AddCourseTextFieldEvent.Reset -> AddCourseTextFieldState()
        }
    }

    private fun AddCourseTextFieldState.toLocalDate(): LocalDate {
        return LocalDate.of(year.toInt(), month.toInt(), day.toInt())
    }

    private fun AddCourseTextFieldState.toTimeString(): LocalTime {
        var hour = hour.toInt()
        if (hour < 7) hour += 12
        return LocalTime.of(hour, minute.toInt())
    }

    private fun AddCourseTextFieldState.isEmpty(): Boolean {
        return day.isEmpty() || month.isEmpty() || year.isEmpty()
                || hour.isEmpty() || minute.isEmpty()
    }

    private fun LocalDateTime.updateFlow(
        flow: MutableStateFlow<AddCourseTextFieldState>,
    ) = flow.update {
        var hour = hour
        if (hour > 12) hour -= 12
        AddCourseTextFieldState(
            year = year.toString(),
            month = monthValue.toString(),
            day = dayOfMonth.toString(),
            hour = hour.toString(),
            minute = minute.toString().padStart(2, '0'),
        )
    }
}
