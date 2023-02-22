package com.ba.schedule.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ba.schedule.domain.model.Course

@Entity(
    tableName = "courses",
)
data class CourseEntity(
    @PrimaryKey val id: Int? = null,
    val name: String,
) {

    fun toCourse() = Course(
        id = id,
        name = name,
    )

    companion object {
        fun fromCourse(course: Course) = CourseEntity(
            id = course.id,
            name = course.name,
        )
    }
}
