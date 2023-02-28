package com.ba.schedule.ui.home.lectures

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Delete
import androidx.compose.material.icons.rounded.Lock
import androidx.compose.material.icons.rounded.LockOpen
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ba.schedule.R
import com.ba.schedule.ui.component.TableCell

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun LecturesScreen(
    viewModel: LecturesViewModel = hiltViewModel(),
    onLectureClick: (Int, Int) -> Unit,
) {

    val tablePadding = 6.dp

    val configuration = LocalConfiguration.current
    val cellWidth = (configuration.screenWidthDp.dp - tablePadding * 5) / 4
    val cellHeight = configuration.screenHeightDp.dp / 11

    val days = viewModel.weekDays

    val isLocked by viewModel.isLayoutLocked.collectAsState()
    val lectures by viewModel.lectures.collectAsState()
    val selectedLectures by viewModel.selectedLectures.collectAsState()
    val lectureTime by viewModel.lectureTime.collectAsState()

    val isRemoveVisible by viewModel.isRemoveVisible.collectAsState()

    val lockColor by animateColorAsState(
        targetValue = if (isLocked) MaterialTheme.colorScheme.error
        else MaterialTheme.colorScheme.primary,
    )

    val lockAngel by animateFloatAsState(
        targetValue = if (isLocked) 0f else 30f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioHighBouncy,
            stiffness = Spring.StiffnessLow,
        ),
    )

    val infiniteTransition = rememberInfiniteTransition()
    val cellScale = infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = .9f,
        animationSpec = infiniteRepeatable(
            tween(durationMillis = 400, easing = LinearEasing),
            RepeatMode.Reverse,
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.lectures)) },
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
                                .padding(4.dp)
                                .rotate(lockAngel),
                        )
                    }
                },
            )
        },
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = tablePadding, vertical = tablePadding * 2),
            horizontalArrangement = Arrangement.spacedBy(tablePadding),
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(tablePadding, Alignment.Bottom),
            ) {
                Box(
                    modifier = Modifier.size(width = cellWidth, height = cellHeight),
                    contentAlignment = Alignment.Center,
                ) {
                    androidx.compose.animation.AnimatedVisibility(
                        visible = isRemoveVisible,
                        enter = scaleIn() + fadeIn(),
                        exit = scaleOut() + fadeOut(),
                    ) {
                        FilledIconButton(
                            onClick = viewModel::onRemoveLecture,
                            shape = MaterialTheme.shapes.large,
                            colors = IconButtonDefaults.filledIconButtonColors(
                                containerColor = MaterialTheme.colorScheme.errorContainer.copy(
                                    alpha = .6f,
                                ),
                            ),
                            modifier = Modifier.fillMaxSize(),
                        ) {
                            Icon(
                                imageVector = Icons.Rounded.Delete,
                                contentDescription = null,
                                modifier = Modifier.size(30.dp),
                            )
                        }
                    }
                }
                viewModel.weekDays.forEach { day ->
                    TableCell(
                        modifier = Modifier.size(width = cellWidth, height = cellHeight),
                        content = day,
                        fontWeight = FontWeight.Medium,
                        containerColor = MaterialTheme.colorScheme.primaryContainer,
                    )
                }
            }
            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(tablePadding),
            ) {
                itemsIndexed(lectureTime) { timeIndex, time ->
                    Column(verticalArrangement = Arrangement.spacedBy(tablePadding)) {
                        TableCell(
                            modifier = Modifier.size(
                                width = cellWidth,
                                height = cellHeight,
                            ),
                            content = "${time.start} - ${time.end}",
                            fontWeight = FontWeight.Medium,
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        )
                        repeat(days.count()) { day ->
                            val lecture = lectures.find { day == it.day && timeIndex == it.time }
                            val isSelected = lecture in selectedLectures
                            TableCell(
                                modifier = Modifier
                                    .size(width = cellWidth, height = cellHeight)
                                    .scale(if (isSelected) cellScale.value else 1f),
                                content = lecture?.course?.name ?: "",
                                enabled = !isLocked,
                                isSelected = isSelected,
                                onClick = {
                                    if (isRemoveVisible) viewModel.onSelectLecture(lecture)
                                    else onLectureClick(day, timeIndex)
                                },
                                onLongClick = { viewModel.onSelectLecture(lecture) },
                            )
                        }
                    }
                }
            }
        }
    }
}