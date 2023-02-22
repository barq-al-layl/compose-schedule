package com.ba.schedule.ui.component

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddCourseDialog(
    isVisible: Boolean,
    value: String,
    onValueChange: (String) -> Unit,
    onConfirm: () -> Unit,
    onDismiss: () -> Unit,
) {
    if (isVisible) {
        AlertDialog(
            onDismissRequest = onDismiss,
            title = {
                Text(text = "ADD COURSE", fontSize = 20.sp, fontWeight = FontWeight.W500)
            },
            text = {
                val focusRequester = remember { FocusRequester() }
                OutlinedTextField(
                    modifier = Modifier.focusRequester(focusRequester),
                    value = value,
                    onValueChange = onValueChange,
                    keyboardOptions = KeyboardOptions(capitalization = KeyboardCapitalization.Words),
                    singleLine = true,
                    textStyle = TextStyle(fontSize = 20.sp),
                    label = { Text(text = "Course name") }
                )
                LaunchedEffect(Unit) {
                    focusRequester.requestFocus()
                }
            },
            confirmButton = {
                TextButton(onClick = onConfirm) {
                    Text(text = "ADD", fontSize = 18.sp)
                }
            },
            dismissButton = {
                TextButton(onClick = onDismiss) {
                    Text(text = "CANCEL", fontSize = 18.sp, color = MaterialTheme.colorScheme.error)
                }
            },
        )
    }
}