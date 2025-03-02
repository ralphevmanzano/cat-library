package com.ralphevmanzano.catlibrary.domain.model.networking

sealed interface NetworkError: Error {
    data object NoInternetConnection: NetworkError
    data object Timeout: NetworkError
    data object TooManyRequests: NetworkError
    data class NotFound(val message: String): NetworkError
    data object Unknown: NetworkError
    data object SerializationError: NetworkError
}