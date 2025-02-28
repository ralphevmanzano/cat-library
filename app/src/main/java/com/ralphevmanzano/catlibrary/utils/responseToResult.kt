package com.ralphevmanzano.catlibrary.utils

import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.Result
import retrofit2.Response

inline fun <reified T> responseToResult(
    response: Response<T>
): Result<T, NetworkError> {
    return if (response.isSuccessful) {
        val body = response.body()
        if (body == null) {
            Result.Error(NetworkError.UNKNOWN)
        } else {
            Result.Success(body)
        }
    } else {
        when (response.code()) {
            404 -> Result.Error(NetworkError.NOT_FOUND)
            408 -> Result.Error(NetworkError.TIMEOUT)
            429 -> Result.Error(NetworkError.TOO_MANY_REQUESTS)
            else -> Result.Error(NetworkError.UNKNOWN)
        }
    }
}