package com.ba.schedule.domain.usecase.exams

import com.ba.schedule.domain.model.Exam
import com.ba.schedule.domain.repository.ExamsRepository
import com.ba.schedule.domain.usecase.FlowUseCase
import com.ba.schedule.domain.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.time.LocalDate
import java.time.LocalTime
import javax.inject.Inject

class GetExamsUseCase @Inject constructor(
    private val repository: ExamsRepository,
) : FlowUseCase<Unit, List<Exam>>() {
    override fun execute(parameters: Unit): Flow<Resource<List<Exam>>> = repository
        .getAll()
        .map { exams ->
            Resource.Success(
                exams.sortedWith(
                    compareBy(
                        { LocalDate.parse(it.date) },
                        { LocalTime.parse(it.time) },
                    )
                )
            )
        }
}