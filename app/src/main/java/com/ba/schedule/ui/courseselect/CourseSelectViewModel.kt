package com.ba.schedule.ui.courseselect

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ba.schedule.data.repository.CoursesRepository
import com.ba.schedule.data.repository.LecturesRepository
import com.ba.schedule.model.Course
import com.ba.schedule.model.Lecture
import com.ba.schedule.ui.navArgs
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

    private val _showSearch = MutableStateFlow(false)
    val showSearch = _showSearch.asStateFlow()

    private val _searchString = MutableStateFlow("")
    val searchString = _searchString.asStateFlow()

    val courses = coursesRepository.getAll()
        .combine(searchString) { courses, search ->
            courses.filter { it.name.contains(search) }
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList(),
        )

    private val _selectedItem = MutableStateFlow<Course?>(null)
    val selectedCourse = _selectedItem.asStateFlow()

    private val day: Int
    private val time: Int

    init {
        val args: CourseSelectScreenNavArgs = savedStateHandle.navArgs()
        day = args.day
        time = args.time
        showSearch.onEach { visible ->
            if (!visible) {
                _searchString.update { "" }
            }
        }.launchIn(viewModelScope)
    }

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

    fun onSearchValueChange(value: String) {
        _searchString.update { value }
    }

    fun onShowSearchChange() {
        _showSearch.update { !it }
    }

}