package com.ba.schedule.ui.component

import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun LectureDurationDialog(
    visible: Boolean,
    initialValue: String,
    onCancel: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    val focusRequester = remember { FocusRequester() }
    val (value, setValue) = remember { mutableStateOf(initialValue) }

    if (visible) {
        AlertDialog(
            onDismissRequest = onCancel,
            confirmButton = {
                FilledIconButton(
                    onClick = {
                        onConfirm(value)
                    },
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Done,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                    )
                }
            },
            dismissButton = {
                FilledIconButton(
                    onClick = onCancel,
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = MaterialTheme.colorScheme.errorContainer,
                    ),
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Close,
                        contentDescription = null,
                        modifier = Modifier.size(30.dp),
                    )
                }

            },
            title = {
                Text(text = "Lecture duration in minutes", fontSize = 18.sp)
            },
            text = {
                OutlinedTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = value,
                    onValueChange = setValue,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    shape = MaterialTheme.shapes.large,
                    textStyle = TextStyle(fontSize = 20.sp)
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            }
        )
    }
}