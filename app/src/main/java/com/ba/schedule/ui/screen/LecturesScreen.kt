package com.ba.schedule.ui.feature.lectures

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ba.schedule.domain.util.Period
import com.ba.schedule.domain.util.WeekDay
import com.ba.schedule.ui.component.TableCell
import com.ba.schedule.ui.viewmodel.LecturesViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LecturesScreen(
    viewModel: LecturesViewModel = hiltViewModel(),
    onLectureClick: (Int, Int) -> Unit,
) {

    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.message.collect {
            val res = snackbarHostState.showSnackbar(
                message = it.message,
                withDismissAction = it.dismissible,
                actionLabel = it.action?.label,
            )
            if (res == SnackbarResult.ActionPerformed) {
                it.action?.perform?.invoke()
            }
        }
    }

    val tablePadding = 6.dp

    val configuration = LocalConfiguration.current
    val cellWidth = (configuration.screenWidthDp.dp - tablePadding * 5) / 4
    val cellHeight = configuration.screenHeightDp.dp / 11

    val days = WeekDay.values()
    val periods = Period.values()

    val isLocked by viewModel.isLayoutLocked.collectAsState()
    val lectures by viewModel.lectures.collectAsState()

    val lockColor by animateColorAsState(
        targetValue = if (isLocked) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.primary,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Lectures") },
                actions = {
                    IconButton(
                        onClick = viewModel::onLayoutLockChange,
                        modifier = Modifier.padding(end = 16.dp),
                    ) {
                        Icon(
                            imageVector = if (isLocked) Icons.Rounded.Lock
                            else Icons.Rounded.LockOpen,
                            contentDescription = null,
                            tint = lockColor,
                            modifier = Modifier
                                .size(38.dp)
                                .padding(4.dp),
                        )
                    }
                }
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState)
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = tablePadding, vertical = tablePadding * 2),
            horizontalArrangement = Arrangement.spacedBy(tablePadding),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(tablePadding),
            ) {
                Spacer(modifier = Modifier.size(width = cellWidth, height = cellHeight))
                days.forEach { day ->
                    TableCell(
                        modifier = Modifier.size(width = cellWidth, height = cellHeight),
                        content = day.name,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                }
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(tablePadding),
            ) {
                items(count = periods.count()) { time ->
                    Column(verticalArrangement = Arrangement.spacedBy(tablePadding)) {
                        TableCell(
                            modifier = Modifier.size(
                                width = cellWidth,
                                height = cellHeight,
                            ),
                            content = periods[time].label,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        )
                        repeat(days.count()) { day ->
                            val lecture = lectures.find { day == it.day && time == it.time }
                            TableCell(
                                modifier = Modifier.size(
                                    width = cellWidth,
                                    height = cellHeight,
                                ),
                                content = lecture?.course?.name ?: "",
                                enabled = !isLocked,
                            ) {
                                onLectureClick(day, time)
                            }
                        }
                    }
                }
            }
        }
    }
}