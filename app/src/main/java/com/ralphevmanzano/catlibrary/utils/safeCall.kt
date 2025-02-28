package com.ralphevmanzano.catlibrary.utils

import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.Result
import kotlinx.coroutines.ensureActive
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(call: () -> Response<T>): Result<T, NetworkError> {
    val response: Response<T> = try {
        call()
    } catch (e: IOException) {
        return Result.Error(NetworkError.NO_INTERNET_CONNECTION)
    } catch (e: Exception) {
        // CancellationException might be thrown here, ensureActive()
        // makes sure it's thrown if the coroutine is cancelled
        coroutineContext.ensureActive()
        return Result.Error(NetworkError.UNKNOWN)
    }

    return responseToResult(response)
}