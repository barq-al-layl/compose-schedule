package com.ba.schedule.ui.feature.lectures

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.domain.model.Course
import com.ba.schedule.domain.model.Lecture
import com.ba.schedule.domain.repository.CoursesRepository
import com.ba.schedule.domain.repository.LecturesRepository
import com.ba.schedule.ui.navigation.MainDestination
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CourseSelectViewModel @Inject constructor(
    savedStateHandle: SavedStateHandle,
    repository: CoursesRepository,
    private val lecturesRepository: LecturesRepository,
) : ViewModel() {

    val courses = repository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.Lazily,
        initialValue = emptyList(),
    )

    private val day: Int
    private val time: Int

    init {
        day = savedStateHandle.get<Int>(MainDestination.kDay)!!
        time = savedStateHandle.get<Int>(MainDestination.kTime)!!
    }

    fun onAddLecture(course: Course) {
        viewModelScope.launch {
            lecturesRepository.add(Lecture(day, time, course))
        }
    }
}