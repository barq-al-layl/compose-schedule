package com.ba.schedule.ui.navigation

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TableView
import androidx.compose.ui.graphics.vector.ImageVector
import com.ba.schedule.R

enum class HomeSection(
    val route: String,
    val icon: ImageVector,
    @StringRes val label: Int,
) {
    Lectures(
        route = "home/lectures",
        icon = Icons.Rounded.TableView,
        label = R.string.lectures,
    ),
    Courses(
        route = "home/courses",
        icon = Icons.Rounded.School,
        label = R.string.courses,
    ),
    Events(
        route = "home/events",
        icon = Icons.Rounded.CalendarMonth,
        label = R.string.events,
    ),
    Settings(
        route = "home/settings",
        icon = Icons.Rounded.Settings,
        label = R.string.settings,
    ),
}