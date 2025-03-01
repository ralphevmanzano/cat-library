package com.ralphevmanzano.catlibrary.utils

import com.ralphevmanzano.catlibrary.data.remote.dto.ErrorDto
import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.Result
import kotlinx.serialization.SerializationException
import kotlinx.serialization.json.Json
import retrofit2.Response

inline fun <reified T> responseToResult(
    response: Response<T>
): Result<T, NetworkError> {
    return if (response.isSuccessful) {
        val body = response.body()
        if (body == null) {
            Result.Error(NetworkError.Unknown)
        } else {
            Result.Success(body)
        }
    } else {
        when (response.code()) {
            404 -> {
                // We can also get the error message from the response body when needed and pass it to the NetworkError
                // if necessary for more detailed error handling
                val apiError = if (response.errorBody() != null) {
                    try {
                        Json.decodeFromString<ErrorDto>(response.errorBody()!!.string())
                    } catch (e: SerializationException) {
                        e.printStackTrace()
                        return Result.Error(NetworkError.SerializationError)
                    }
                } else {
                    return Result.Error(NetworkError.Unknown)
                }
                Result.Error(NetworkError.NotFound(apiError.message))
            }
            408 -> Result.Error(NetworkError.Timeout)
            429 -> Result.Error(NetworkError.TooManyRequests)
            else -> Result.Error(NetworkError.Unknown)
        }
    }
}