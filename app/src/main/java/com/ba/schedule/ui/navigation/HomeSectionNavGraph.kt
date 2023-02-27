package com.ba.schedule.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ba.schedule.ui.ScheduleAppState
import com.ba.schedule.ui.courses.CoursesScreen
import com.ba.schedule.ui.exams.EventsScreen
import com.ba.schedule.ui.lectures.LecturesScreen
import com.ba.schedule.ui.settings.SettingsScreen

fun NavGraphBuilder.homeSectionNavGraph(
    appState: ScheduleAppState,
) {
    navigation(
        route = MainDestination.Home,
        startDestination = HomeSection.Lectures.route,
    ) {
        composable(
            route = HomeSection.Lectures.route,
        ) {
            LecturesScreen(onLectureClick = appState::navigateToCourseSelect)
        }
        composable(
            route = HomeSection.Courses.route,
        ) {
            CoursesScreen(navigateToAddCourse = appState::navigateToAddCourse)
        }
        composable(
            route = HomeSection.Events.route,
        ) {
            EventsScreen()
        }
        composable(
            route = HomeSection.Settings.route,
        ) {
            SettingsScreen()
        }
    }

}