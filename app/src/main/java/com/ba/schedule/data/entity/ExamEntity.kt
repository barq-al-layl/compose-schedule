package com.ba.schedule.data.entity

import androidx.room.Entity
import com.ba.schedule.model.Exam
import com.ba.schedule.model.ExamType
import java.time.LocalDate
import java.time.LocalTime

@Entity(
    tableName = "exams",
    primaryKeys = ["courseId", "type"],
)
data class ExamEntity(
    val courseId: Int,
    val date: LocalDate,
    val time: LocalTime,
    val type: ExamType,
) {
    companion object {
        fun fromExam(exam: Exam) = ExamEntity(
            courseId = exam.course!!.id!!,
            date = exam.date,
            time = exam.time,
            type = exam.type,
        )
    }
}
