package com.ba.schedule.ui.courseselect

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
    coursesRepository: CoursesRepository,
    private val lecturesRepository: LecturesRepository,
) : ViewModel() {

    val courses = coursesRepository.getAll().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList(),
    )

    private val _selectedItem = MutableStateFlow<Course?>(null)
    val selectedCourse = _selectedItem.asStateFlow()

    private val day: Int = savedStateHandle[MainDestination.kDay]!!
    private val time: Int = savedStateHandle[MainDestination.kTime]!!

    fun onItemSelect(item: Course) {
        _selectedItem.update {
            if (it != item) item
            else null
        }
    }

    fun onAddLecture() {
        viewModelScope.launch {
            val lecture = Lecture(
                day = day,
                time = time,
                course = selectedCourse.value ?: return@launch,
            )
            lecturesRepository.add(lecture)
        }
    }
}