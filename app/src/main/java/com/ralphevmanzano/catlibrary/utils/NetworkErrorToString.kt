package com.ralphevmanzano.catlibrary.utils

import android.content.Context
import com.ralphevmanzano.catlibrary.R
import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError

fun NetworkError.toString(context: Context): String {
    return when (this) {
        is NetworkError.NotFound -> this.message
        NetworkError.NoInternetConnection -> context.getString(R.string.network_error_no_internet)
        NetworkError.Timeout -> context.getString(R.string.network_error_timeout)
        NetworkError.TooManyRequests -> context.getString(R.string.network_error_too_many_requests)
        NetworkError.Unknown -> context.getString(R.string.network_error_unknown)
        NetworkError.SerializationError -> context.getString(R.string.couldn_t_parse_the_response)
    }
}