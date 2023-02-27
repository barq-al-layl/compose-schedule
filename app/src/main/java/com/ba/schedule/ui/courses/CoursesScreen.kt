package com.ba.schedule.ui.courses

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.ExperimentalFoundationApi
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ba.schedule.R
import com.ba.schedule.ui.component.CourseCard

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun CoursesScreen(
    viewModel: CoursesViewModel = hiltViewModel(),
    navigateToAddCourse: (Int) -> Unit,
) {
    val showSearch by viewModel.showSearch.collectAsState()
    val searchValue by viewModel.searchString.collectAsState()
    val courses by viewModel.courses.collectAsState()
    val expandedItem by viewModel.expandedItem.collectAsState()

    val screenWidth = LocalConfiguration.current.screenWidthDp.dp - 32.dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.courses)) },
                actions = {
                    IconButton(
                        onClick = viewModel::onShowSearchChange,
                        modifier = Modifier.padding(end = 16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Search,
                            contentDescription = null,
                            modifier = Modifier.size(30.dp),
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
                    placeholder = { Text(text = stringResource(id = R.string.search)) },
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
        floatingActionButton = {
            FloatingActionButton(onClick = { navigateToAddCourse(-1) }) {
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
                    onEdit = { navigateToAddCourse(it.id!!) },
                    onDelete = { viewModel.onDeleteCourse(it) },
                    modifier = Modifier.animateItemPlacement()
                )
            }
        }
    }
}