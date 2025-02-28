package com.ralphevmanzano.catlibrary.data.remote

import com.ralphevmanzano.catlibrary.data.remote.dto.CatDto
import retrofit2.Response
import retrofit2.http.GET

interface CatService {

    @GET("/breeds")
    suspend fun getCats(): Response<List<CatDto>>
}