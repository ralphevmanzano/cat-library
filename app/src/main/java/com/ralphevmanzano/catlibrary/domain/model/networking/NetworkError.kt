package com.ralphevmanzano.catlibrary.domain.model.networking

enum class NetworkError: Error {
    NO_INTERNET_CONNECTION,
    TIMEOUT,
    TOO_MANY_REQUESTS,
    NOT_FOUND,
    UNKNOWN
}