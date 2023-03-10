package com.ba.schedule.ui

import android.content.res.Resources
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.SnackbarResult
import androidx.compose.runtime.Composable
import androidx.compose.runtime.ReadOnlyComposable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import com.ba.schedule.model.SnackbarManager
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Composable
fun rememberScheduleAppState(
    snackbarHostState: SnackbarHostState = remember { SnackbarHostState() },
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    resources: Resources = resources(),
    snackbarManager: SnackbarManager = SnackbarManager,
): ScheduleAppState = remember(
    snackbarHostState,
    resources,
    coroutineScope,
    snackbarManager,
) {
    ScheduleAppState(
        snackbarHostState = snackbarHostState,
        coroutineScope = coroutineScope,
        snackbarManager = snackbarManager,
        resources = resources,
    )
}

class ScheduleAppState(
    val snackbarHostState: SnackbarHostState,
    val resources: Resources,
    coroutineScope: CoroutineScope,
    private val snackbarManager: SnackbarManager,
) {

    init {
        coroutineScope.launch {
            snackbarManager.messages.collect { currentMessages ->
                if (currentMessages.isNotEmpty()) {
                    val item = currentMessages.first()
                    val result = snackbarHostState.showSnackbar(
                        message = resources.getString(item.message),
                        actionLabel = item.action?.label?.let { resources.getString(it) },
                        withDismissAction = item.dismissible,
                    )
                    if (result == SnackbarResult.ActionPerformed) {
                        item.action!!.perform()
                    }
                    snackbarManager.setMessageAsShown(item.id)
                }
            }
        }
    }
}

@Composable
@ReadOnlyComposable
fun resources(): Resources {
    LocalConfiguration.current
    return LocalContext.current.resources
}
