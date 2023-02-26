package com.ba.schedule.domain.usecase

import android.util.Log
import com.ba.schedule.domain.util.Resource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers

abstract class UseCase<in P, R>(
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.Default,
) {

    suspend operator fun invoke(parameters: P): Resource<R> {
        return try {
            with(coroutineDispatcher) {
                execute(parameters).let {
                    Resource.Success(it)
                }
            }
        } catch (e: Exception) {
            Log.d(TAG, e.toString())
            Resource.Error(e)
        }
    }

    @Throws(RuntimeException::class)
    protected abstract suspend fun execute(parameters: P): R

    companion object {
        const val TAG = "UseCase"
    }
}