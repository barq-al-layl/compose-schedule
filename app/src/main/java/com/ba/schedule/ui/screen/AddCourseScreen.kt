package com.ba.schedule.ui.screen

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ba.schedule.ui.component.AddCourseTextField
import com.ba.schedule.ui.viewmodel.AddCourseViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseScreen(
    viewModel: AddCourseViewModel = hiltViewModel(),
    navigateBack: () -> Unit,
) {
    val courseName by viewModel.courseName.collectAsState()
    val final by viewModel.final.collectAsState()
    val midterm by viewModel.midterm.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Add Course") },
                navigationIcon = {
                    IconButton(onClick = navigateBack) {
                        Icon(
                            imageVector = Icons.Rounded.ArrowBackIosNew,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                },
                actions = {
                    FilledIconButton(
                        onClick = {
                            viewModel.onAddCourse().also {
                                if (it) navigateBack()
                            }
                        },
                        modifier = Modifier.padding(4.dp),
                        shape = MaterialTheme.shapes.large,
                        colors = IconButtonDefaults.filledIconButtonColors(
                            containerColor = MaterialTheme.colorScheme.tertiaryContainer,
                        ),
                    ) {
                        Icon(
                            imageVector = Icons.Rounded.Save,
                            contentDescription = null,
                            modifier = Modifier.size(28.dp),
                        )
                    }
                },
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .padding(innerPadding),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = courseName,
                onValueChange = viewModel::onNameChange,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 18.sp),
                label = { Text(text = "Course Name") },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(onNext = {}),
                shape = MaterialTheme.shapes.large,
            )
            AddCourseTextField(
                label = "Final",
                state = final,
                onEvent = viewModel::onFinalChange,
            )
            AddCourseTextField(
                label = "Midterm",
                state = midterm,
                onEvent = viewModel::onMidtermChange,
            )
        }
    }
}