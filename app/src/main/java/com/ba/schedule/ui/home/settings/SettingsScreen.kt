@file:OptIn(ExperimentalMaterial3Api::class)

package com.ba.schedule.ui.home.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowForwardIos
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import com.ba.schedule.R
import com.ba.schedule.model.ThemeMode
import com.ramcosta.composedestinations.annotation.Destination
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

@Destination
@Composable
fun SettingsScreen(
    viewModel: SettingViewModel = hiltViewModel(),
) {
    val themeMode by viewModel.themeMode.collectAsState()
    val useDynamicColors by viewModel.useDynamicColors.collectAsState()
    val lecturesPerDay by viewModel.totalLectures.collectAsState()
    val lectureDuration by viewModel.lectureDuration.collectAsState()
    val startTime by viewModel.startTime.collectAsState()

    var isSelectThemeDialogVisible by remember { mutableStateOf(false) }
    var isTimePickerDialogVisible by remember { mutableStateOf(false) }
    var isLectureDurationDialogVisible by remember { mutableStateOf(false) }

    val lecturesPerDayOptions = List(6) { "${it + 1}" }
    var isLecturesPerDayExpanded by remember { mutableStateOf(false) }

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
                .padding(16.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
            ElevatedCard(
                onClick = { isSelectThemeDialogVisible = true },
                shape = MaterialTheme.shapes.medium,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.default_theme))
                    IconButton(onClick = { isSelectThemeDialogVisible = true }) {
                        Icon(imageVector = Icons.Rounded.ArrowForwardIos, contentDescription = null)
                    }
                }
            }
            ElevatedCard(
                onClick = {
                    viewModel.onUseDynamicColorChanged(!useDynamicColors)
                },
                shape = MaterialTheme.shapes.medium,
                enabled = viewModel.supportDynamicColor,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.dynamic_colors))
                    Switch(
                        checked = useDynamicColors,
                        onCheckedChange = viewModel::onUseDynamicColorChanged,
                        enabled = viewModel.supportDynamicColor
                    )
                }
            }
            ElevatedCard(
                onClick = { isLecturesPerDayExpanded = true },
                shape = MaterialTheme.shapes.medium,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.lectures_per_day))
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
            }
            ElevatedCard(
                onClick = { isTimePickerDialogVisible = true },
                shape = MaterialTheme.shapes.medium,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.start_time))

                    TextButton(onClick = { isTimePickerDialogVisible = true }) {
                        Text(
                            text = startTime.format(
                                DateTimeFormatter.ofLocalizedTime(FormatStyle.SHORT),
                            ),
                        )
                    }
                }
            }
            ElevatedCard(
                onClick = { isLectureDurationDialogVisible = true },
                shape = MaterialTheme.shapes.medium,
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 8.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(text = stringResource(id = R.string.lecture_duration))

                    TextButton(onClick = { isLectureDurationDialogVisible = true }) {
                        Text(
                            text = "$lectureDuration ${stringResource(id = R.string.minute)}"
                        )
                    }
                }
            }
        }
    }
    SelectThemeDialog(
        visible = isSelectThemeDialogVisible,
        initialValue = themeMode,
        onCancel = { isSelectThemeDialogVisible = false },
        onConfirm = {
            viewModel.onThemeModeChanged(it)
            isSelectThemeDialogVisible = false
        }
    )
    TimePickerDialog(
        visible = isTimePickerDialogVisible,
        initialTime = startTime,
        onCancel = {
            isTimePickerDialogVisible = false
        },
        onConfirm = { hour, minute ->
            viewModel.onStartTimeChanged(hour, minute)
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

@Composable
private fun SelectThemeDialog(
    visible: Boolean,
    initialValue: ThemeMode,
    onCancel: () -> Unit,
    onConfirm: (ThemeMode) -> Unit,
) {
    if (visible) {
        val (selectedState, onSelect) = remember { mutableStateOf(initialValue) }
        AlertDialog(
            onDismissRequest = onCancel,
            title = {
                Text(text = stringResource(id = R.string.change_theme))
            },
            text = {
                Column(
                    modifier = Modifier.selectableGroup(),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    ThemeMode.values().forEach {
                        Surface(
                            onClick = { onSelect(it) },
                            modifier = Modifier.fillMaxWidth(),
                            shape = MaterialTheme.shapes.medium,
                        ) {
                            Row(
                                horizontalArrangement = Arrangement.spacedBy(12.dp),
                                verticalAlignment = Alignment.CenterVertically,
                            ) {
                                RadioButton(
                                    selected = it == selectedState,
                                    onClick = { onSelect(it) },
                                )
                                Text(
                                    text = stringResource(id = it.label),
                                    fontSize = 16.sp,
                                )
                            }
                        }
                    }
                }
            },
            dismissButton = {
                TextButton(onClick = onCancel) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            confirmButton = {
                TextButton(onClick = { onConfirm(selectedState) }) {
                    Text(text = stringResource(id = R.string.accept))
                }
            },
        )
    }
}

@Composable
private fun TimePickerDialog(
    visible: Boolean,
    initialTime: LocalTime,
    onCancel: () -> Unit,
    onConfirm: (Int, Int) -> Unit,
) {
    if (visible) {
        val state = rememberTimePickerState(
            initialHour = initialTime.hour,
            initialMinute = initialTime.minute,
        )
        Dialog(onDismissRequest = onCancel) {
            Column(
                modifier = Modifier
                    .clip(MaterialTheme.shapes.large)
                    .background(MaterialTheme.colorScheme.surfaceColorAtElevation(3.dp))
                    .padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.End,
            ) {
                TimePicker(state = state)
                Row(
                    modifier = Modifier.fillMaxWidth(.6f),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    FilledIconButton(
                        modifier = Modifier.weight(1f),
                        onClick = onCancel,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.errorContainer,
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                        )
                    }
                    FilledIconButton(
                        modifier = Modifier.weight(1f),
                        onClick = { onConfirm(state.hour, state.minute) },
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Done,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
                        )
                    }
                }

            }
        }
    }
}

@Composable
private fun LectureDurationDialog(
    visible: Boolean,
    initialValue: String,
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    if (visible) {
        val focusRequester = remember { FocusRequester() }
        val (value, setValue) = remember {
            mutableStateOf(
                TextFieldValue(
                    initialValue,
                    TextRange(initialValue.length),
                )
            )
        }

        AlertDialog(
            onDismissRequest = onCancel,
            confirmButton = {
                TextButton(onClick = { onConfirm(value.text) }) {
                    Text(text = stringResource(id = R.string.accept))
                }
            },
            dismissButton = {
                TextButton(onClick = onCancel) {
                    Text(text = stringResource(id = R.string.cancel))
                }
            },
            title = {
                Text(
                    text = stringResource(id = R.string.lecture_duration_in_minutes),
                    fontSize = 18.sp,
                )
            },
            text = {
                OutlinedTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = value,
                    onValueChange = setValue,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large,
                    textStyle = TextStyle(fontSize = 20.sp),
                    keyboardActions = KeyboardActions(onDone = { onConfirm(value.text) })
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        )
    }
}