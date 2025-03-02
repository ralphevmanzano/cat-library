package com.ralphevmanzano.catlibrary.domain.repository

import com.ralphevmanzano.catlibrary.domain.model.Cat
import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.Result
import kotlinx.coroutines.flow.Flow

interface CatRepository {
    suspend fun getCats(): Flow<Result<List<Cat>, NetworkError>>
    suspend fun getCatDetails(id: String): Flow<Result<Cat, NetworkError>>
}