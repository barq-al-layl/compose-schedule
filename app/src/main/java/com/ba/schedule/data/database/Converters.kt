package com.ba.schedule.data.database

import androidx.room.TypeConverter
import com.ba.schedule.model.ExamType
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

class Converters {

    @TypeConverter
    fun fromLocalDate(date: LocalDate): String {
        return date.toString()
    }

    @TypeConverter
    fun toLocalDate(value: String): LocalDate {
        return LocalDate.parse(value)
    }

    @TypeConverter
    fun fromLocalTime(time: LocalTime): String {
        return time.format(DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    }

    @TypeConverter
    fun toLocalTime(value: String): LocalTime {
        return LocalTime.parse(value, DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT))
    }

    @TypeConverter
    fun fromExamType(type: ExamType): Int = type.ordinal

    @TypeConverter
    fun toExamType(value: Int): ExamType = ExamType.values()[value]
}