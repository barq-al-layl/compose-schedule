package com.ba.schedule.domain.util

import com.ba.schedule.domain.util.Resource.Success
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update

sealed class Resource<out R> {
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val exception: Exception? = null) : Resource<Nothing>()
}

val <T> Resource<T>.data: T?
    get() = (this as? Success)?.data

inline fun <reified T> Resource<T>.updateOnSuccess(stateFlow: MutableStateFlow<T>) {
    if (this is Success) {
        stateFlow.update { data }
    }
}