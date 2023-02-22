package com.ba.schedule.ui.feature.lectures

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CourseSelectScreen(
    viewModel: CourseSelectViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val courses by viewModel.courses.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Select Course", fontSize = 20.sp) }
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            items(items = courses, key = { it.id!! }) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                        .clip(MaterialTheme.shapes.large)
                        .background(MaterialTheme.colorScheme.tertiaryContainer)
                        .clickable {
                            viewModel.onAddLecture(it)
                            navigateBack()
                        },
                    contentAlignment = Alignment.CenterStart,
                ) {
                    Text(text = it.name, fontSize = 22.sp)
                }
            }
        }
    }
}