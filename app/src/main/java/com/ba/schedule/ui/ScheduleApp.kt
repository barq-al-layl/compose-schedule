package com.ba.schedule.ui

import androidx.annotation.StringRes
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.CalendarMonth
import androidx.compose.material.icons.rounded.School
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.TableView
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.rememberNavController
import com.ba.schedule.R
import com.ba.schedule.ui.destinations.*
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.ramcosta.composedestinations.DestinationsNavHost
import com.ramcosta.composedestinations.navigation.navigate
import com.ramcosta.composedestinations.spec.DirectionDestinationSpec

@Composable
fun ScheduleApp() {
    val navController = rememberNavController()
    val appState = rememberScheduleAppState()
    val systemUiController = rememberSystemUiController()
    Scaffold(
        bottomBar = {
            val currentDestination by navController.appCurrentDestinationAsState()
            val bottomBarDirections = HomeSection.values().map { it.direction }
            val showBottomBar = bottomBarDirections.any { it == currentDestination }
            val backgroundColor = MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp)
            val systemBarColor = animateColorAsState(
                targetValue = if (showBottomBar) backgroundColor
                else MaterialTheme.colorScheme.background,
                animationSpec = spring(stiffness = Spring.StiffnessMedium)
            )
            systemUiController.setNavigationBarColor(systemBarColor.value)
            AnimatedVisibility(
                visible = showBottomBar,
                enter = slideInVertically(tween()) { it },
                exit = slideOutVertically(tween()) { it },
            ) {
                BottomBar(
                    backgroundColor = backgroundColor,
                    currentDestination = currentDestination,
                ) {
                    if (it != currentDestination) {
                        navController.navigate(it) {
                            launchSingleTop = true
                            restoreState = true
                            popUpTo(0) { saveState = true }
                        }
                    }
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = appState.snackbarHostState)
        }
    ) { innerPadding ->
        DestinationsNavHost(
            navGraph = NavGraphs.root,
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            navController = navController,
        )
    }
}

enum class HomeSection(
    val direction: DirectionDestinationSpec,
    val icon: ImageVector,
    @StringRes val label: Int,
) {
    Lectures(
        direction = LecturesScreenDestination,
        icon = Icons.Rounded.TableView,
        label = R.string.lectures,
    ),
    Courses(
        direction = CoursesScreenDestination,
        icon = Icons.Rounded.School,
        label = R.string.courses,
    ),
    Events(
        direction = EventsScreenDestination,
        icon = Icons.Rounded.CalendarMonth,
        label = R.string.exams,
    ),
    Settings(
        direction = SettingsScreenDestination,
        icon = Icons.Rounded.Settings,
        label = R.string.settings,
    ),
}

@Composable
private fun BottomBar(
    backgroundColor: Color,
    currentDestination: Destination?,
    onItemClick: (DirectionDestinationSpec) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(14.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        HomeSection.values().forEach {
            val isSelected = it.direction == currentDestination
            CustomBottomBarItem(
                selected = isSelected,
                item = it,
            ) { onItemClick(it.direction) }
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