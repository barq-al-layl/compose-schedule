package com.ba.schedule.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ba.schedule.domain.model.Course
import java.time.LocalDateTime

@Entity(
    tableName = "courses",
)
data class CourseEntity(
    @PrimaryKey val id: Int? = null,
    val name: String,
    val final_: LocalDateTime,
    val midterm: LocalDateTime,
) {

    fun toCourse() = Course(
        id = id,
        name = name,
        final = final_.takeUnless { it == LocalDateTime.MIN },
        midterm = midterm.takeUnless { it == LocalDateTime.MIN },
    )

    companion object {
        fun fromCourse(course: Course) = CourseEntity(
            id = course.id,
            name = course.name,
            final_ = course.final ?: LocalDateTime.MIN,
            midterm = course.midterm ?: LocalDateTime.MIN,
        )
    }
}
