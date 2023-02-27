package com.ba.schedule.domain.model

sealed class EditCourseTextFieldEvent {
    data class YearChange(val value: String) : EditCourseTextFieldEvent()
    data class MonthChange(val value: String) : EditCourseTextFieldEvent()
    data class DayChange(val value: String) : EditCourseTextFieldEvent()
    data class HourChange(val value: String) : EditCourseTextFieldEvent()
    data class MinuteChange(val value: String) : EditCourseTextFieldEvent()
    object Reset : EditCourseTextFieldEvent()
}
