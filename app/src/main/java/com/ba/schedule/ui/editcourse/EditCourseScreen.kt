package com.ba.schedule.ui.editcourse

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowBackIosNew
import androidx.compose.material.icons.rounded.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ba.schedule.R
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.navigation.DestinationsNavigator

data class EditCourseScreenNavArgs(val id: Int = -1)

@OptIn(ExperimentalMaterial3Api::class)
@Destination(navArgsDelegate = EditCourseScreenNavArgs::class)
@Composable
fun EditCourseScreen(
    navigator: DestinationsNavigator,
    viewModel: EditCourseViewModel = hiltViewModel(),
) {
    val courseName by viewModel.courseName.collectAsState()
    val final by viewModel.final.collectAsState()
    val midterm by viewModel.midterm.collectAsState()

    val focusManager = LocalFocusManager.current

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = stringResource(id = R.string.add_course)) },
                navigationIcon = {
                    IconButton(onClick = navigator::navigateUp) {
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
                            val res = viewModel.onEditCourse()
                            if (res) navigator.navigateUp()
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
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 24.dp, vertical = 8.dp)
                .verticalScroll(rememberScrollState()),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            OutlinedTextField(
                value = courseName,
                onValueChange = viewModel::onNameChanged,
                modifier = Modifier.fillMaxWidth(),
                textStyle = TextStyle(fontSize = 18.sp),
                label = { Text(text = stringResource(id = R.string.course_name)) },
                keyboardOptions = KeyboardOptions(
                    capitalization = KeyboardCapitalization.Words,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = { focusManager.moveFocus(FocusDirection.Next) },
                ),
                shape = MaterialTheme.shapes.large,
            )
            AddCourseTextField(
                label = R.string.final_,
                state = final,
                focusManager = focusManager,
                onEvent = viewModel::onFinalChanged,
            )
            AddCourseTextField(
                label = R.string.midterm,
                state = midterm,
                focusManager = focusManager,
                onEvent = viewModel::onMidtermChanged,
            )
        }
    }
}
