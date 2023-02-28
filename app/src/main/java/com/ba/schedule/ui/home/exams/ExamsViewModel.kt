package com.ba.schedule.ui.home.exams

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.model.ExamType
import com.ba.schedule.data.repository.ExamsRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

@HiltViewModel
class ExamsViewModel @Inject constructor(
    repository: ExamsRepository,
) : ViewModel() {

    private val timeFormatter = DateTimeFormatter.ofPattern("h:mm")
    private val dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd\nEEEE")

    private val _examType = MutableStateFlow(ExamType.Final)
    val examType = _examType.asStateFlow()

    val exams = repository.getAll()
        .combine(examType) { exams, type ->
            exams.filter { it.type == type }
        }.toStateFlow(emptyList())

    val localTimes = exams.map { exams ->
        exams.fold(listOf<LocalTime>()) { acc, exam ->
            acc + exam.time
        }.distinct().sorted()
    }.toStateFlow(emptyList())

    val formattedTime = localTimes.map {
        it.map(timeFormatter::format)
    }.toStateFlow(emptyList())

    val localDates = exams.map { exams ->
        exams.fold(listOf<LocalDate>()) { acc, exam ->
            acc + exam.date
        }.distinct().sorted()
    }.toStateFlow(emptyList())

    val formattedDate = localDates.map {
        it.map(dateFormatter::format)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    fun onTypeChanged() = _examType.update {
        when (it) {
            ExamType.Final -> ExamType.Midterm
            ExamType.Midterm -> ExamType.Final
        }
    }

    private fun <T> Flow<T>.toStateFlow(initialValue: T): StateFlow<T> = this.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = initialValue
    )
}