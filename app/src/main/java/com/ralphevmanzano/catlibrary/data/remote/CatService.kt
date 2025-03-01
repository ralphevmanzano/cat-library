package com.ralphevmanzano.catlibrary.data.remote

import com.ralphevmanzano.catlibrary.data.remote.dto.CatDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface CatService {

    @GET("breeds")
    suspend fun getCats(): Response<List<CatDto>>

    @GET("breeds/{id}")
    suspend fun getCatDetails(@Path("id") id: String): Response<CatDto>
}