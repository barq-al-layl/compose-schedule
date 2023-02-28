package com.ba.schedule.ui.home.settings

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ba.schedule.R
import com.ba.schedule.domain.model.ThemeMode
import com.ba.schedule.ui.component.LectureDurationDialog
import com.ba.schedule.ui.component.SettingsListItem
import com.ba.schedule.ui.component.TimePickerDialog
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SettingViewModel = hiltViewModel()) {
    val themeMode by viewModel.themeMode.collectAsState()
    val useDynamicColors by viewModel.useDynamicColors.collectAsState()
    val lecturesPerDay by viewModel.totalLectures.collectAsState()
    val lectureDuration by viewModel.lectureDuration.collectAsState()
    val startTime by viewModel.startTime.collectAsState()

    var isMenuExpanded by remember { mutableStateOf(false) }
    var isTimePickerDialogVisible by remember { mutableStateOf(false) }
    var isLectureDurationDialogVisible by remember { mutableStateOf(false) }

    val lecturesPerDayOptions = List(6) { "${it + 1}" }
    var isLecturesPerDayExpanded by remember { mutableStateOf(false) }

    val timePickerState = rememberTimePickerState(
        initialHour = startTime.hour,
        initialMinute = startTime.minute,
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.settings)) }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            SettingsListItem("Theme mode") {
                Box {
                    TextButton(onClick = { isMenuExpanded = true }) {
                        Text(text = themeMode.name)
                    }
                    DropdownMenu(
                        expanded = isMenuExpanded,
                        onDismissRequest = { isMenuExpanded = false },
                    ) {
                        ThemeMode.values().forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = it.name
                                    )
                                },
                                onClick = {
                                    viewModel.onThemeModeChanged(it)
                                    isMenuExpanded = false
                                },
                            )
                        }
                    }

                }
            }
            Divider()
            SettingsListItem("Use dynamic colors (Android 12+)") {
                Switch(
                    checked = useDynamicColors,
                    onCheckedChange = viewModel::onUseDynamicColorChanged,
                    enabled = viewModel.supportDynamicColor
                )
            }
            Divider()
            SettingsListItem("Lectures per day") {
                Box {
                    TextButton(onClick = { isLecturesPerDayExpanded = true }) {
                        Text(
                            text = "$lecturesPerDay",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                    DropdownMenu(
                        expanded = isLecturesPerDayExpanded,
                        onDismissRequest = { isLecturesPerDayExpanded = false },
                    ) {
                        lecturesPerDayOptions.forEach {
                            DropdownMenuItem(
                                text = {
                                    Text(
                                        text = it,
                                        fontSize = 18.sp,
                                        fontWeight = FontWeight.Medium,
                                    )
                                },
                                onClick = {
                                    viewModel.onLecturesPerDayChanged(it.toInt())
                                    isLecturesPerDayExpanded = false
                                },
                            )
                        }
                    }

                }
            }
            Divider()
            SettingsListItem("Lectures starts at") {
                TextButton(onClick = { isTimePickerDialogVisible = true }) {
                    Text(
                        text = startTime.format(
                            DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT),
                        ),
                    )
                }
            }
            Divider()
            SettingsListItem("Lecture duration in minutes") {
                TextButton(onClick = { isLectureDurationDialogVisible = true }) {
                    Text(
                        text = lectureDuration
                    )
                }
            }
        }
    }
    TimePickerDialog(
        visible = isTimePickerDialogVisible,
        timePickerState = timePickerState,
        onCancel = {
            isTimePickerDialogVisible = false
        },
        onConfirm = {
            viewModel.onStartTimeChanged(timePickerState.hour, timePickerState.minute)
            isTimePickerDialogVisible = false
        },
    )
    LectureDurationDialog(
        visible = isLectureDurationDialogVisible,
        initialValue = lectureDuration,
        onCancel = { isLectureDurationDialogVisible = false },
        onConfirm = {
            val res = viewModel.onLectureDurationChanged(it)
            isLectureDurationDialogVisible = !res
        }
    )
}