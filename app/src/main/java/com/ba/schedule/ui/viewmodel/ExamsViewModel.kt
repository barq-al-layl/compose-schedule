package com.ba.schedule.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.domain.model.ExamType
import com.ba.schedule.domain.usecase.exams.GetExamsUseCase
import com.ba.schedule.domain.util.data
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import javax.inject.Inject

@HiltViewModel
class ExamsViewModel @Inject constructor(
    getExamsUseCase: GetExamsUseCase,
) : ViewModel() {

    private val _examType = MutableStateFlow(ExamType.Final)
    val examType = _examType.asStateFlow()

    val exams = getExamsUseCase(Unit)
        .mapNotNull { it.data }
        .combine(examType) { exams, type ->
            exams.filter { it.type == type }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

    val header = exams.map { exams ->
        exams.fold(listOf<String>()) { acc, exam ->
            acc + exam.time
        }.distinct()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    val dates = exams.map { exams ->
        exams.fold(listOf<String>()) { acc, exam ->
            acc + exam.date
        }.distinct()
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
}