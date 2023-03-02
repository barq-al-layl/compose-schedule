package com.ba.schedule.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.ba.schedule.ui.courseselect.CourseSelectScreen
import com.ba.schedule.ui.editcourse.EditCourseScreen
import com.ba.schedule.ui.navigation.HomeSection
import com.ba.schedule.ui.navigation.MainDestination
import com.ba.schedule.ui.navigation.homeSectionNavGraph
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
fun ScheduleApp() {
    val appState = rememberScheduleAppState()
    val systemUiController = rememberSystemUiController()
    Scaffold(
        bottomBar = {
            val showBottomBar = appState.shouldShowBottomBar
            val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            val systemBarColor = animateColorAsState(
                targetValue = if (showBottomBar) backgroundColor
                else MaterialTheme.colorScheme.background,
                animationSpec = spring(
                    stiffness = Spring.StiffnessMedium,
                )
            )
            systemUiController.setNavigationBarColor(systemBarColor.value)
            AnimatedVisibility(
                visible = appState.shouldShowBottomBar,
                enter = slideInVertically(tween()) { it },
                exit = slideOutVertically(tween()) { it },
            ) {
                BottomBar(
                    backgroundColor = backgroundColor,
                    tabs = appState.bottomBarTabs,
                    currentRoute = appState.currentRoute!!,
                    navigateToRoute = appState::navigateToBottomBarRoute,
                )
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = appState.snackbarHostState)
        }
    ) { innerPadding ->
        NavHost(
            navController = appState.navController,
            startDestination = MainDestination.Home,
            modifier = Modifier.padding(innerPadding),
        ) {
            homeSectionNavGraph(appState)
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
            composable(
                route = "${MainDestination.AddCourse}/{${MainDestination.kCourseId}}",
                arguments = listOf(
                    navArgument(MainDestination.kCourseId) {
                        type = NavType.IntType
                        defaultValue = -1
                    },
                ),
            ) {
                EditCourseScreen(navigateBack = appState::upPress)
            }
        }
    }
}

@Composable
private fun BottomBar(
    backgroundColor: Color,
    tabs: Array<HomeSection>,
    currentRoute: String,
    navigateToRoute: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        tabs.forEach {
            CustomBottomBarItem(
                selected = it.route == currentRoute,
                item = it,
            ) {
                navigateToRoute(it.route)
            }
        }
    }
}

@Composable
private fun CustomBottomBarItem(
    selected: Boolean,
    item: HomeSection,
    onClick: () -> Unit,
) {
    val contentColor = if (selected) MaterialTheme.colorScheme.primary
    else MaterialTheme.colorScheme.onBackground

    val backgroundColor = if (selected) contentColor.copy(alpha = .25f) else Color.Transparent
    Row(
        modifier = Modifier
            .clip(CircleShape)
            .border(width = 2.dp, color = backgroundColor, shape = CircleShape)
            .clickable { onClick() }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = item.icon,
            contentDescription = null,
            modifier = Modifier.size(26.dp),
            tint = contentColor,
        )
        AnimatedVisibility(visible = selected) {
            Text(
                text = stringResource(id = item.label),
                color = contentColor,
            )
        }
    }
}