package com.ralphevmanzano.catlibrary.data.repository

import com.ralphevmanzano.catlibrary.data.repository.datasource.CatRemoteDataSource
import com.ralphevmanzano.catlibrary.domain.model.Cat
import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.Result
import com.ralphevmanzano.catlibrary.domain.repository.CatRepository

class CatRepositoryImpl(private val remoteDataSource: CatRemoteDataSource): CatRepository {
    override suspend fun getCats(): Result<List<Cat>, NetworkError> {
        // TODO add logic to handle caching here
        return remoteDataSource.getCats()
    }

    override suspend fun getCatDetails(id: String): Result<Cat, NetworkError> {
        // TODO add logic to handle caching here
        return remoteDataSource.getCatDetails(id)
    }
}