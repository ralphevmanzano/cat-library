package com.ralphevmanzano.catlibrary.domain.model.networking

//enum class NetworkError: Error {
//    NO_INTERNET_CONNECTION,
//    TIMEOUT,
//    TOO_MANY_REQUESTS,
//    NOT_FOUND,
//    UNKNOWN
//}

sealed interface NetworkError: Error {
    data object NoInternetConnection: NetworkError
    data object Timeout: NetworkError
    data object TooManyRequests: NetworkError
    data class NotFound(
        val message: String
    ): NetworkError
    data object Unknown: NetworkError
    data object SerializationError: NetworkError
}