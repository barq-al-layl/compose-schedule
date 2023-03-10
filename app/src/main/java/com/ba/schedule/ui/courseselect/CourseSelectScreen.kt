package com.ba.schedule.ui.courseselect

import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ba.schedule.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

data class CourseSelectScreenNavArgs(val day: Int, val time: Int)

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = CourseSelectScreenNavArgs::class)
@Composable
fun CourseSelectScreen(
    navigator: DestinationsNavigator,
    viewModel: CourseSelectViewModel = hiltViewModel(),
) {
    val courses by viewModel.courses.collectAsState()
    val selectedCourse by viewModel.selectedCourse.collectAsState()
    val cardWith = LocalConfiguration.current.screenWidthDp.dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(id = R.string.select_course),
                        fontSize = 22.sp,
                        fontWeight = FontWeight.W500,
                    )
                },
                actions = {
                    IconButton(onClick = navigator::navigateUp) {
                        Icon(
                            imageVector = Icons.Rounded.Close,
                            contentDescription = null,
                            modifier = Modifier.size(32.dp),
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        if (courses.isEmpty()) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                Text(text = stringResource(id = R.string.nothing_to_show), fontSize = 20.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.padding(innerPadding),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(items = courses, key = { it.id!! }) {
                    val isSelected = it == selectedCourse
                    val animatedCardWidth by animateDpAsState(
                        targetValue = if (isSelected) cardWith * .7f else cardWith,
                    )
                    Row(
                        modifier = Modifier.height(IntrinsicSize.Max),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        Box(
                            modifier = Modifier
                                .width(animatedCardWidth)
                                .clip(MaterialTheme.shapes.large)
                                .background(MaterialTheme.colorScheme.secondaryContainer)
                                .clickable { viewModel.onItemSelect(it) }
                                .padding(horizontal = 16.dp, vertical = 10.dp),
                            contentAlignment = Alignment.CenterStart,
                        ) {
                            Text(
                                text = it.name,
                                fontSize = 20.sp,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis,
                            )
                        }
                        FilledIconButton(
                            onClick = {
                                viewModel.onAddLecture()
                                navigator.navigateUp()
                            },
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxHeight(),
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                            ),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Done,
                                contentDescription = null,
                                modifier = Modifier.size(32.dp),
                            )
                        }
                    }
                }
            }
        }
    }
}