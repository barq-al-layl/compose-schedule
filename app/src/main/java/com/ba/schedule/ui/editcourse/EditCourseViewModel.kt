package com.ba.schedule.ui.editcourse

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.domain.model.*
import com.ba.schedule.domain.usecase.courses.AddCourseParameter
import com.ba.schedule.domain.usecase.courses.EditCourseUseCase
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
class EditCourseViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    private val getCourseByIdUseCase: GetCourseByIdUseCase,
    private val editCourseUseCase: EditCourseUseCase,
    private val getExamsForCourseUseCase: GetExamsForCourseUseCase,
    private val editExamUseCase: EditExamUseCase,
    private val removeExamUseCase: RemoveExamUseCase,
) : ViewModel() {

    private val _courseName = MutableStateFlow("")
    val courseName = _courseName.asStateFlow()

    private val _final = MutableStateFlow(EditCourseTextFieldState())
    val final = _final.asStateFlow()

    private val _midterm = MutableStateFlow(EditCourseTextFieldState())
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

    fun onEditCourse(): Boolean {
        val name = courseName.value.trim()
        if (name.isEmpty()) return false
        viewModelScope.launch {
            var course = Course(
                id = courseId.takeIf { it != -1 },
                name = name,
            )
            val courseId =
                editCourseUseCase(AddCourseParameter(course)).data?.toInt() ?: return@launch
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
                editExamUseCase(AddExamUseCaseParameter(exam))
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
                editExamUseCase(AddExamUseCaseParameter(exam))
            }
        }
        return true
    }

    fun onNameChanged(value: String) {
        _courseName.update { value }
    }

    fun onFinalChanged(event: EditCourseTextFieldEvent) {
        event.updateFlow(_final)
    }

    fun onMidtermChanged(event: EditCourseTextFieldEvent) {
        event.updateFlow(_midterm)
    }

    private fun EditCourseTextFieldEvent.updateFlow(
        flow: MutableStateFlow<EditCourseTextFieldState>,
    ) = flow.update {
        when (this) {
            is EditCourseTextFieldEvent.YearChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x !in 1..2300 && value.isNotEmpty()) return
                it.copy(year = value)
            }
            is EditCourseTextFieldEvent.MonthChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x !in 1..12 && value.isNotEmpty()) return
                it.copy(month = value)
            }
            is EditCourseTextFieldEvent.DayChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x !in 1..31 && value.isNotEmpty()) return
                it.copy(day = value)
            }
            is EditCourseTextFieldEvent.HourChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x !in 1..12 && value.isNotEmpty()) return
                it.copy(hour = value)
            }
            is EditCourseTextFieldEvent.MinuteChange -> {
                val x = value.toIntOrNull() ?: 0
                if (x !in 1..59 && value.length > 2) return
                it.copy(minute = value)
            }
            EditCourseTextFieldEvent.Reset -> EditCourseTextFieldState()
        }
    }

    private fun EditCourseTextFieldState.toLocalDate(): LocalDate {
        return LocalDate.of(year.toInt(), month.toInt(), day.toInt())
    }

    private fun EditCourseTextFieldState.toTimeString(): LocalTime {
        var hour = hour.toInt()
        if (hour < 7) hour += 12
        return LocalTime.of(hour, minute.toInt())
    }

    private fun EditCourseTextFieldState.isEmpty(): Boolean {
        return day.isEmpty() || month.isEmpty() || year.isEmpty()
                || hour.isEmpty() || minute.isEmpty()
    }

    private fun LocalDateTime.updateFlow(
        flow: MutableStateFlow<EditCourseTextFieldState>,
    ) = flow.update {
        var hour = hour
        if (hour > 12) hour -= 12
        EditCourseTextFieldState(
            year = year.toString(),
            month = monthValue.toString(),
            day = dayOfMonth.toString(),
            hour = hour.toString(),
            minute = minute.toString().padStart(2, '0'),
        )
    }
}