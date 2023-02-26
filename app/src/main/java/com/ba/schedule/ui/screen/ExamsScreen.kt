package com.ba.schedule.ui.screen

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ba.schedule.R
import com.ba.schedule.domain.model.ExamType
import com.ba.schedule.ui.component.TableCell
import com.ba.schedule.ui.viewmodel.ExamsViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EventsScreen(
    viewModel: ExamsViewModel = hiltViewModel(),
) {
    val examType by viewModel.examType.collectAsState()
    val exams by viewModel.exams.collectAsState()
    val times by viewModel.header.collectAsState()
    val dates by viewModel.dates.collectAsState()

    val tablePadding = 8.dp
    val configuration = LocalConfiguration.current

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
                    IconButton(onClick = viewModel::onTypeChanged) {
                        Icon(
                            imageVector = Icons.Rounded.SwapHoriz,
                            contentDescription = null,
                            modifier = Modifier
                                .size(32.dp)
                                .rotate(iconAngel),
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(tablePadding),
            horizontalArrangement = Arrangement.spacedBy(tablePadding),
        ) {
            Column(
                modifier = Modifier.weight(2f),
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
                dates.forEach {
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
            times.forEach { time ->
                Column(
                    modifier = Modifier.weight(2f),
                    verticalArrangement = Arrangement.spacedBy(tablePadding),
                ) {
                    TableCell(
                        content = time,
                        modifier = Modifier
                            .height(cellHeight)
                            .fillMaxWidth(),
                        containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        fontWeight = FontWeight.Medium,
                    )
                    dates.forEach { date ->
                        val exam = exams.find { it.date == date && it.time == time }
                        TableCell(
                            content = exam?.course?.name ?: "",
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