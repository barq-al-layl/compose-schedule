package com.ba.schedule.ui

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ba.schedule.model.SnackbarManager
import com.ba.schedule.ui.navigation.HomeSection
import com.ba.schedule.ui.navigation.MainDestination
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberScheduleAppState(
    navController: NavHostController = rememberNavController(),
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    resources: Resources = resources(),
    snackbarManager: SnackbarManager = SnackbarManager,
): ScheduleAppState = remember(
    navController,
    snackbarHostState,
    resources,
    coroutineScope,
    snackbarManager,
) {
    ScheduleAppState(
        navController = navController,
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope,
        snackbarManager = snackbarManager,
        resources = resources,
    )
}

class ScheduleAppState(
    val navController: NavHostController,
    val snackbarHostState: SnackbarHostState,
    val resources: Resources,
    coroutineScope: CoroutineScope,
    private val snackbarManager: SnackbarManager,
) {

    val bottomBarTabs = HomeSection.values()
    private val bottomBarRoutes = bottomBarTabs.map { it.route }

    val shouldShowBottomBar: Boolean
        @Composable get() = navController
            .currentBackStackEntryAsState().value?.destination?.route in bottomBarRoutes

    val currentRoute: String?
        get() = navController.currentDestination?.route

    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val item = currentMessages.first()
                    val result = snackbarHostState.showSnackbar(
                        message = resources.getString(item.message),
                        actionLabel = item.action?.label?.let { resources.getString(it) },
                        withDismissAction = item.dismissible,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        item.action!!.perform()
                    }
                    snackbarManager.setMessageAsShown(item.id)
                }
            }
        }
    }

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

    fun navigateToAddCourse(id: Int) {
        navController.navigate("${MainDestination.AddCourse}/$id")
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

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
