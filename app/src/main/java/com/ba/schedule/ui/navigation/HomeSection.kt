package com.ba.schedule.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TableView
import androidx.compose.ui.graphics.vector.ImageVector

enum class HomeSection(
    val route: String,
    val icon: ImageVector,
) {
    Lectures(
        route = "home/lectures",
        icon = Icons.Rounded.TableView,
    ),
    Courses(
        route = "home/courses",
        icon = Icons.Rounded.School,
    ),
    Events(
        route = "home/events",
        icon = Icons.Rounded.CalendarMonth,
    ),
    Settings(
        route = "home/settings",
        icon = Icons.Rounded.Settings,
    ),
}