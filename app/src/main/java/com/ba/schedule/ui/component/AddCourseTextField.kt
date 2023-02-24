package com.ba.schedule.ui.component

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ba.schedule.ui.util.AddCourseTextFieldEvent
import com.ba.schedule.ui.util.AddCourseTextFieldState

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseTextField(
    label: String,
    state: AddCourseTextFieldState,
    onEvent: (AddCourseTextFieldEvent) -> Unit,
) {
    val focusManager = LocalFocusManager.current

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = .55f),
                shape = MaterialTheme.shapes.large,
            )
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        Text(
            text = label,
            fontSize = 18.sp,
            fontWeight = FontWeight.W500,
        )
        Row(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = state.day,
                onValueChange = { onEvent(AddCourseTextFieldEvent.DayChange(it)) },
                modifier = Modifier.weight(2f),
                textStyle = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center),
                label = { Text(text = "DD") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                ),
                shape = MaterialTheme.shapes.large,
            )
            OutlinedTextField(
                value = state.month,
                onValueChange = { onEvent(AddCourseTextFieldEvent.MonthChange(it)) },
                modifier = Modifier.weight(2f),
                textStyle = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center),
                label = { Text(text = "MM") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                ),
                shape = MaterialTheme.shapes.large,
            )
            OutlinedTextField(
                value = state.year,
                onValueChange = { onEvent(AddCourseTextFieldEvent.YearChange(it)) },
                modifier = Modifier.weight(3f),
                textStyle = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center),
                label = { Text(text = "YYYY") },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                ),
                shape = MaterialTheme.shapes.large,
            )
        }
        Row(
            modifier = Modifier.height(IntrinsicSize.Max),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            OutlinedTextField(
                value = state.hour,
                onValueChange = { onEvent(AddCourseTextFieldEvent.HourChange(it)) },
                modifier = Modifier.weight(2f),
                textStyle = TextStyle(
                    fontSize = 18.sp,
                    fontWeight = FontWeight.W500,
                    textAlign = TextAlign.Center
                ),
                label = { Text(text = "HOUR", fontSize = 14.sp) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Next,
                ),
                keyboardActions = KeyboardActions(
                    onNext = {
                        focusManager.moveFocus(FocusDirection.Next)
                    },
                ),
                shape = MaterialTheme.shapes.large,
            )
            OutlinedTextField(
                value = state.minute,
                onValueChange = { onEvent(AddCourseTextFieldEvent.MinuteChange(it)) },
                modifier = Modifier.weight(2f),
                textStyle = TextStyle(fontSize = 18.sp, textAlign = TextAlign.Center),
                label = { Text(text = "MINUTE", fontSize = 14.sp) },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Number,
                    imeAction = ImeAction.Done,
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        focusManager.clearFocus()
                    },
                ),
                shape = MaterialTheme.shapes.large,
            )
            FilledIconButton(
                onClick = { onEvent(AddCourseTextFieldEvent.Reset) },
                modifier = Modifier
                    .fillMaxHeight()
                    .weight(1f)
                    .padding(vertical = 6.dp),
                shape = MaterialTheme.shapes.large,
                colors = IconButtonDefaults.filledIconButtonColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                ),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Refresh,
                    contentDescription = null,
                )
            }
        }
    }
}
