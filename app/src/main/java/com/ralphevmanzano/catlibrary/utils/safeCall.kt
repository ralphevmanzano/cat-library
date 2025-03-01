package com.ralphevmanzano.catlibrary.utils

import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.Result
import kotlinx.coroutines.ensureActive
import kotlinx.serialization.SerializationException
import retrofit2.Response
import java.io.IOException
import kotlin.coroutines.coroutineContext

suspend inline fun <reified T> safeCall(call: () -> Response<T>): Result<T, NetworkError> {
    val response: Response<T> = try {
        call()
    } catch (e: IOException) {
        return Result.Error(NetworkError.NoInternetConnection)
    } catch (e: SerializationException) {
        e.printStackTrace()
        return Result.Error(NetworkError.SerializationError)
    }
    catch (e: Exception) {
        // CancellationException might be thrown here, ensureActive()
        // makes sure it's thrown if the coroutine is cancelled
        coroutineContext.ensureActive()
        e.printStackTrace()
        return Result.Error(NetworkError.Unknown)
    }

    return responseToResult(response)
}