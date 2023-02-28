package com.ba.schedule.data.entity

import androidx.room.Embedded
import androidx.room.Relation
import com.ba.schedule.model.Lecture

data class CourseWithLectures(
    @Embedded val course: CourseEntity,
    @Relation(
        parentColumn = "id",
        entityColumn = "courseId",
    )
    val lectures: List<LectureEntity>,
) {
    fun toLectures() = lectures.map {
        Lecture(
            day = it.day,
            time = it.time,
            course = course.toCourse(),
        )
    }
}
