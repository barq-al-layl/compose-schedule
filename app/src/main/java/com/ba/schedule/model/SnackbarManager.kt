package com.ba.schedule.model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.update

object SnackbarManager {

    private val _messages = MutableStateFlow(emptyList<SnackbarMessage>())
    val messages = _messages.asSharedFlow()

    fun showMessage(message: SnackbarMessage) {
        _messages.update { it + message }
    }

    fun setMessageAsShown(id: Long) {
        _messages.update { items ->
            items.filterNot { id == it.id }
        }
    }
}