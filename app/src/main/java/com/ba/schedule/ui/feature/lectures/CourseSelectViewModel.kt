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
import kotlinx.coroutines.flow.*
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

    private val _selectedItem = MutableStateFlow<Course?>(null)
    val selectedCourse = _selectedItem.asStateFlow()

    private val day: Int
    private val time: Int

    init {
        day = savedStateHandle[MainDestination.kDay]!!
        time = savedStateHandle[MainDestination.kTime]!!
    }

    fun onItemSelect(item: Course) {
        _selectedItem.update {
            if (it != item) item
            else null
        }
    }

    fun onAddLecture() {
        viewModelScope.launch {
            lecturesRepository.add(
                Lecture(
                    day = day,
                    time = time,
                    course = selectedCourse.value ?: return@launch,
                )
            )
        }
    }
}