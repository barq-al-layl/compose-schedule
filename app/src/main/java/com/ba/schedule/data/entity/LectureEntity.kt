package com.ba.schedule.data.entity

import androidx.room.Entity
import com.ba.schedule.domain.model.Lecture

@Entity(
    tableName = "lectures",
    primaryKeys = ["day", "time"],
)
data class LectureEntity(
    val day: Int,
    val time: Int,
    val courseId: Int,
) {

    companion object {
        fun fromLecture(lecture: Lecture) = LectureEntity(
            day = lecture.day,
            time = lecture.time,
            courseId = lecture.course.id!!,
        )
    }
}
