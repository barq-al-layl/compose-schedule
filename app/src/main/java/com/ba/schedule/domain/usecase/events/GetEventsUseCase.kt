package com.ba.schedule.domain.usecase.events

import com.ba.schedule.domain.model.Event
import com.ba.schedule.domain.model.EventType
import com.ba.schedule.domain.usecase.FlowUseCase
import com.ba.schedule.domain.usecase.courses.GetCoursesUseCase
import com.ba.schedule.domain.util.Resource
import com.ba.schedule.domain.util.data
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.mapNotNull
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class GetEventsUseCase @Inject constructor(
    private val getCoursesUseCase: GetCoursesUseCase,
) : FlowUseCase<GetEventsUseCaseParameter, List<Event>>() {
    override fun execute(parameters: GetEventsUseCaseParameter): Flow<Resource<List<Event>>> {
        val datePattern = "uuuu-MM-dd\nEEE"
        val timePattern = "hh:mm"
        val dateFormatter = DateTimeFormatter.ofPattern(datePattern)
        val timeFormatter = DateTimeFormatter.ofPattern(timePattern)
        return getCoursesUseCase(Unit)
            .mapNotNull { it.data }
            .map { courses ->
                val events = mutableMapOf<String, Event>()
                courses.sortedBy {
                    when (parameters.type) {
                        EventType.Finals -> it.final
                        EventType.Midterms -> it.midterm
                    }
                }.forEach { course ->
                    val type = when (parameters.type) {
                        EventType.Finals -> course.final
                        EventType.Midterms -> course.midterm
                    }
                    val date = dateFormatter.format(type)
                    val time = timeFormatter.format(type)
                    val event = events.getOrDefault(
                        key = date,
                        defaultValue = Event(date = date),
                    )
                    events[date] = event.copy(
                        time = event.time + time,
                        courses = event.courses + course,
                    )
                }
                Resource.Success(events.values.toList())
            }
    }
}

data class GetEventsUseCaseParameter(val type: EventType)