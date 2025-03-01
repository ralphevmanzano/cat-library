package com.ralphevmanzano.catlibrary.data.remote

import com.ralphevmanzano.catlibrary.BuildConfig
import okhttp3.Interceptor
import okhttp3.Response

/**
 * Interceptor class for providing api_key parameter to CatService
 */
class ApiKeyInterceptor: Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        var original = chain.request()
        val url = original.url.newBuilder().addQueryParameter("api_key", BuildConfig.CAT_API_KEY).build()
        original = original.newBuilder().url(url).build()
        return chain.proceed(original)
    }
}