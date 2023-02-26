package com.ba.schedule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.domain.model.ExamType
import com.ba.schedule.domain.usecase.exams.*
import com.ba.schedule.domain.util.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

@HiltViewModel
class ExamsViewModel @Inject constructor(
    getExamsUseCase: GetExamsUseCase,
    formatDateUseCase: FormatDateUseCase,
    formatTimeUseCase: FormatTimeUseCase,
) : ViewModel() {

    private val _examType = MutableStateFlow(ExamType.Final)
    val examType = _examType.asStateFlow()

    val exams = getExamsUseCase(Unit)
        .mapNotNull { it.data }
        .combine(examType) { exams, type ->
            exams.filter { it.type == type }
        }.toStateFlow(emptyList())

    val localTimes = exams.map { exams ->
        exams.fold(listOf<LocalTime>()) { acc, exam ->
            acc + exam.time
        }.distinct().sorted()
    }.toStateFlow(emptyList())

    val formattedTime = localTimes.map {
        it.mapNotNull { time ->
            formatTimeUseCase(FormatTimeUseCaseParameter(time)).data
        }
    }.toStateFlow(emptyList())

    val localDates = exams.map { exams ->
        exams.fold(listOf<LocalDate>()) { acc, exam ->
            acc + exam.date
        }.distinct().sorted()
    }.toStateFlow(emptyList())

    val formattedDate = localDates.map {
        it.mapNotNull { date ->
            formatDateUseCase(FormatDateUseCaseParameter(date)).data
        }
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