package com.ba.schedule.ui.feature.courses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ba.schedule.ui.component.AddCourseDialog
import com.ba.schedule.ui.component.CourseCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CoursesScreen(
    viewModel: CoursesViewModel = hiltViewModel(),
) {
    val showSearch by viewModel.showSearch.collectAsState()
    val searchValue by viewModel.searchString.collectAsState()
    val courses by viewModel.courses.collectAsState()
    val expandedItem by viewModel.expandedItem.collectAsState()
    val showDialog by viewModel.showDialog.collectAsState()
    val dialogValue by viewModel.dialogValue.collectAsState()

    val snackbarHosState = remember { SnackbarHostState() }
    val screenWidth = LocalConfiguration.current.screenWidthDp.dp - 32.dp

    LaunchedEffect(Unit) {
        viewModel.message.collect {
            val res = snackbarHosState.showSnackbar(
                it.message,
                withDismissAction = it.dismissible,
                actionLabel = it.action?.label,
            )
            if (res == SnackbarResult.ActionPerformed) {
                it.action?.perform?.invoke()
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Courses") },
                actions = {
                    IconButton(onClick = viewModel::onShowSearchChange) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                },
            )
            AnimatedVisibility(
                visible = showSearch,
                enter = slideInVertically { -it },
                exit = slideOutVertically { -it },
            ) {
                val focusRequester = remember { FocusRequester() }
                TextField(
                    value = searchValue,
                    onValueChange = viewModel::onSearchValueChange,
                    modifier = Modifier
                        .focusRequester(focusRequester)
                        .height(64.dp)
                        .fillMaxWidth(),
                    singleLine = true,
                    placeholder = { Text(text = "Type something...") },
                    trailingIcon = {
                        IconButton(onClick = viewModel::onShowSearchChange) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = null,
                                modifier = Modifier.size(28.dp),
                            )
                        }
                    },
                    textStyle = TextStyle(fontSize = 20.sp),
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHosState)
        },
        floatingActionButton = {
            FloatingActionButton(onClick = viewModel::onShowDialogChange) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = null,
                    modifier = Modifier.size(28.dp),
                )
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(
                start = 16.dp,
                top = 16.dp,
                end = 16.dp,
                bottom = 96.dp,
            ),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(
                items = courses,
                key = { it.id!! },
            ) {
                CourseCard(
                    item = it,
                    width = screenWidth,
                    expanded = expandedItem == it.id,
                    onClick = { viewModel.onExpandItem(it.id!!) },
                    onEdit = { viewModel.onEditCourse(it) },
                    onDelete = { viewModel.onDeleteCourse(it) },
                )
            }
        }
    }
    AddCourseDialog(
        isVisible = showDialog,
        value = dialogValue,
        onValueChange = viewModel::onDialogValueChange,
        onConfirm = viewModel::onAddCourse,
        onDismiss = viewModel::onShowDialogChange,
    )
}