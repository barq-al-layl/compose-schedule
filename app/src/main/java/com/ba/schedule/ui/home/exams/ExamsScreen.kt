package com.ba.schedule.ui.home.exams

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.SwapHoriz
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ba.schedule.R
import com.ba.schedule.model.ExamType
import com.ba.schedule.ui.component.TableCell

@OptIn(ExperimentalMaterial3Api::class, ExperimentalAnimationApi::class)
@Composable
fun EventsScreen(
    viewModel: ExamsViewModel = hiltViewModel(),
) {
    val examType by viewModel.examType.collectAsState()
    val exams by viewModel.exams.collectAsState()
    val localTimes by viewModel.localTimes.collectAsState()
    val localDates by viewModel.localDates.collectAsState()
    val formattedDate by viewModel.formattedDate.collectAsState()
    val formattedTime by viewModel.formattedTime.collectAsState()

    val tablePadding = 8.dp
    val configuration = LocalConfiguration.current

    val cellWidth = (configuration.screenWidthDp.dp - tablePadding * 4) / 3

    val cellHeight = (configuration.screenHeightDp.dp -
            tablePadding * (exams.size + 2)) / 10

    val iconAngel by animateFloatAsState(
        targetValue = if (examType == ExamType.Final) 0f else 180f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow,
        )
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.exams)) },
                actions = {
                    IconButton(
                        onClick = viewModel::onTypeChanged,
                        modifier = Modifier.padding(end = 16.dp),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.SwapHoriz,
                            contentDescription = null,
                            modifier = Modifier
                                .padding(4.dp)
                                .size(38.dp)
                                .rotate(iconAngel),
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center,
        ) {
            Row(
                modifier = Modifier.padding(tablePadding),
                horizontalArrangement = Arrangement.spacedBy(tablePadding),
            ) {
                Column(
                    modifier = Modifier.width(cellWidth),
                    verticalArrangement = Arrangement.spacedBy(tablePadding),
                ) {
                    Box(
                        modifier = Modifier
                            .height(cellHeight)
                            .fillMaxWidth(),
                        contentAlignment = Alignment.Center,
                    ) {
                        Text(
                            text = stringResource(
                                id = when (examType) {
                                    ExamType.Final -> R.string.final_
                                    ExamType.Midterm -> R.string.midterm
                                }
                            ),
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Medium,
                            textAlign = TextAlign.Center,
                        )
                    }
                    formattedDate.forEach {
                        TableCell(
                            content = it,
                            modifier = Modifier
                                .height(cellHeight)
                                .fillMaxWidth(),
                            containerColor = MaterialTheme.colorScheme.primaryContainer,
                            fontWeight = FontWeight.Medium,
                        )
                    }
                }
                LazyRow(
                    modifier = Modifier.clip(MaterialTheme.shapes.small),
                    horizontalArrangement = Arrangement.spacedBy(tablePadding),
                ) {
                    itemsIndexed(localTimes) { index, time ->
                        Column(
                            modifier = Modifier.width(cellWidth),
                            verticalArrangement = Arrangement.spacedBy(tablePadding),
                        ) {
                            TableCell(
                                content = formattedTime[index],
                                modifier = Modifier
                                    .height(cellHeight)
                                    .fillMaxWidth(),
                                containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                                fontWeight = FontWeight.Medium,
                            )
                            localDates.forEach { date ->
                                val exam = exams.filter { date == it.date && it.time == time }
                                if (exam.isNotEmpty()) {
                                    Column(
                                        modifier = Modifier.height(cellHeight),
                                        verticalArrangement = Arrangement.spacedBy(tablePadding),
                                    ) {
                                        exam.forEach {
                                            TableCell(
                                                content = it.course?.name ?: "",
                                                modifier = Modifier
                                                    .weight(1f)
                                                    .fillMaxWidth(),
                                            )
                                        }
                                    }
                                } else {
                                    TableCell(
                                        content = "",
                                        modifier = Modifier
                                            .height(cellHeight)
                                            .fillMaxWidth(),
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}