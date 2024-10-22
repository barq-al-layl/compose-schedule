package com.ba.schedule.data.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.ba.schedule.model.Exam

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
            date = it.date,
            time = it.time,
            type = it.type,
        )
    }
}
