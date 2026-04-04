package com.vinstall.alwiz.installer

import com.vinstall.alwiz.util.DebugLog
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.withTimeoutOrNull

object InstallHelper {

    sealed class Result {
        object Success : Result()
        data class Failure(val message: String?) : Result()
    }

    private val _results = MutableSharedFlow<Result>(replay = 1, extraBufferCapacity = 1)

    fun emit(result: Result) {
        DebugLog.d("InstallHelper", "emit: $result")
        _results.tryEmit(result)
    }

    @OptIn(kotlinx.coroutines.ExperimentalCoroutinesApi::class)
    fun reset() {
        _results.resetReplayCache()
    }

    suspend fun awaitResult(timeoutMs: Long = 300_000L): Result? =
        withTimeoutOrNull(timeoutMs) {
            _results.first()
        }
}
