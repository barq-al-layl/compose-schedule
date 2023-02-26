package com.ba.schedule.data.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.ba.schedule.domain.model.Exam
import java.time.format.DateTimeFormatter

data class CourseWithExams(
    @Embedded val course: CourseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "courseId",
    )
    val exams: List<ExamEntity>,
) {
    fun toExams() = exams.map {
        Exam(
            course = course.toCourse(),
            date = dateFormatter.format(it.date),
            time = it.time.toString(),
            type = it.type,
        )
    }

    companion object {
        private val dateFormatter = DateTimeFormatter.ofPattern("uuuu-MM-dd\nEEEE")
    }
}
