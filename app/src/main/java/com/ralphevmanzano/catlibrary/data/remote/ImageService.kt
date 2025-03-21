package com.ralphevmanzano.catlibrary.data.remote

import okhttp3.ResponseBody
import retrofit2.http.GET
import retrofit2.http.Streaming
import retrofit2.http.Url

interface ImageService {

    @Streaming
    @GET
    suspend fun downloadFile(@Url url: String): ResponseBody
}