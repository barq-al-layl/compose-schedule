package com.ba.schedule.ui.screen

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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ba.schedule.ui.viewmodel.CourseSelectViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseSelectScreen(
    viewModel: CourseSelectViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val courses by viewModel.courses.collectAsState()
    val selectedCourse by viewModel.selectedCourse.collectAsState()
    val cardWith = LocalConfiguration.current.screenWidthDp.dp

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Select Course",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.W500,
                    )
                },
                actions = {
                    IconButton(onClick = navigateBack) {
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
                            navigateBack()
                        },
                        modifier = Modifier.weight(1f).fillMaxHeight(),
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