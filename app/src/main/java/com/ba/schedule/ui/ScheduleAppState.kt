package com.ba.schedule.ui

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ba.schedule.ui.navigation.HomeSection
import com.ba.schedule.ui.navigation.MainDestination

@Composable
fun rememberScheduleAppState(
    navController: NavHostController = rememberNavController(),
): ScheduleAppState = remember(navController) {
    ScheduleAppState(navController = navController)
}

class ScheduleAppState(
    val navController: NavHostController,
) {

    val bottomBarTabs = HomeSection.values()
    private val bottomBarRoutes = bottomBarTabs.map { it.route }

    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

    val currentRoute: String?
        get() = navController.currentDestination?.route

    fun navigateToBottomBarRoute(route: String) {
        if (route != currentRoute) {
            navController.navigate(route) {
                launchSingleTop = true
                restoreState = true
                popUpTo(findStartDestination(navController.graph).id) {
                    saveState = true
                }
            }
        }
    }

    fun navigateToCourseSelect(day: Int, time: Int) {
        navController.navigate("${MainDestination.SelectCourse}/$day/$time")
    }

    fun upPress() {
        navController.navigateUp()
    }
}

private val NavGraph.startDestination: NavDestination?
    get() = findNode(startDestinationId)

private tailrec fun findStartDestination(graph: NavDestination): NavDestination {
    return if (graph is NavGraph) findStartDestination(graph.startDestination!!) else graph
}
