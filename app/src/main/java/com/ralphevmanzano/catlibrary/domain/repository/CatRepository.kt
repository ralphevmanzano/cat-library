package com.ralphevmanzano.catlibrary.domain.repository

import com.ralphevmanzano.catlibrary.domain.model.Cat
import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.Result

interface CatRepository {
    suspend fun getCats(): Result<List<Cat>, NetworkError>
    suspend fun getCatDetails(id: String): Result<Cat, NetworkError>
}