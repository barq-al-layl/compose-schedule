package com.ba.schedule.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ba.schedule.ui.component.ScheduleBottomBar
import com.ba.schedule.ui.screen.CourseSelectScreen
import com.ba.schedule.ui.navigation.MainDestination
import com.ba.schedule.ui.navigation.homeSectionNavGraph

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ScheduleApp() {
    val appState = rememberScheduleAppState()
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = appState.shouldShowBottomBar,
                enter = slideInVertically { it },
                exit = slideOutVertically { it },
            ) {
                ScheduleBottomBar(
                    tabs = appState.bottomBarTabs,
                    currentRoute = appState.currentRoute!!,
                    navigateToRoute = appState::navigateToBottomBarRoute,
                )
            }
        }
    ) { innerPadding ->
        NavHost(
            navController = appState.navController,
            startDestination = MainDestination.Home,
            modifier = Modifier.padding(innerPadding),
        ) {
            homeSectionNavGraph(appState::navigateToCourseSelect)
            composable(
                route = "${MainDestination.SelectCourse}/{${MainDestination.kDay}}/{${MainDestination.kTime}}",
                arguments = listOf(
                    navArgument(MainDestination.kDay) {
                        type = NavType.IntType
                    },
                    navArgument(MainDestination.kTime) {
                        type = NavType.IntType
                    },
                ),
            ) {
                CourseSelectScreen(navigateBack = appState::upPress)
            }
        }
    }
}