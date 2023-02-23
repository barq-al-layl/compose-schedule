package com.ba.schedule.ui.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import com.ba.schedule.ui.screen.CoursesScreen
import com.ba.schedule.ui.screen.EventsScreen
import com.ba.schedule.ui.feature.lectures.LecturesScreen
import com.ba.schedule.ui.screen.SettingsScreen

fun NavGraphBuilder.homeSectionNavGraph(
    navigateToCourseSelect: (Int, Int) -> Unit,
) {
    navigation(
        route = MainDestination.Home,
        startDestination = HomeSection.Lectures.route,
    ) {
        composable(
            route = HomeSection.Lectures.route,
        ) {
            LecturesScreen(onLectureClick = navigateToCourseSelect)
        }
        composable(
            route = HomeSection.Courses.route,
        ) {
            CoursesScreen()
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