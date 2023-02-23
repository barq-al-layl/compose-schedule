package com.ba.schedule.ui.util

sealed class AddCourseTextFieldEvent {
    data class YearChange(val value: String) : AddCourseTextFieldEvent()
    data class MonthChange(val value: String) : AddCourseTextFieldEvent()
    data class DayChange(val value: String) : AddCourseTextFieldEvent()
    data class HourChange(val value: String) : AddCourseTextFieldEvent()
    data class MinuteChange(val value: String) : AddCourseTextFieldEvent()
    object Reset : AddCourseTextFieldEvent()
}
