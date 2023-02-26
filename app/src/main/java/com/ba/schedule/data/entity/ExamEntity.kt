package com.ba.schedule.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ba.schedule.domain.model.Exam
import com.ba.schedule.domain.model.ExamType
import java.time.LocalDate
import java.time.LocalTime

@Entity(tableName = "exams")
data class ExamEntity(
    @PrimaryKey val courseId: Int,
    val date: LocalDate,
    val time: LocalTime,
    val type: ExamType,
) {
    companion object {
        fun fromExam(exam: Exam) = ExamEntity(
            courseId = exam.course!!.id!!,
            date = LocalDate.parse(exam.date),
            time = LocalTime.parse(exam.time),
            type = exam.type,
        )
    }
}
