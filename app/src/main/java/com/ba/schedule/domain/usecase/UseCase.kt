package com.ba.schedule.domain.usecase

import android.util.Log
import com.ba.schedule.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher

abstract class UseCase<in P, R>(private val coroutineDispatcher: CoroutineDispatcher) {

    suspend operator fun invoke(parameters: P): Resource<R> {
        return try {
            with(coroutineDispatcher) {
                execute(parameters).let {
                    Resource.Success(it)
                }
            }
        } catch (e: Exception) {
            Log.d("UseCase ", e.toString())
            Resource.Error(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R
}