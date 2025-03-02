package com.ralphevmanzano.catlibrary.data.repository

import com.ralphevmanzano.catlibrary.data.mappers.toCat
import com.ralphevmanzano.catlibrary.data.mappers.toCatEntity
import com.ralphevmanzano.catlibrary.data.repository.datasource.CatLocalDataSource
import com.ralphevmanzano.catlibrary.data.repository.datasource.CatRemoteDataSource
import com.ralphevmanzano.catlibrary.domain.model.Cat
import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.Result
import com.ralphevmanzano.catlibrary.domain.repository.CatRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class CatRepositoryImpl(
    private val remoteDataSource: CatRemoteDataSource,
    private val localDataSource: CatLocalDataSource
): CatRepository {

    override suspend fun getCats(): Flow<Result<List<Cat>, NetworkError>> = flow {
        val localCats = localDataSource.getCats()
        if (localCats.isNotEmpty()) {
            emit(Result.Success(localCats.map { it.toCat() }))
        }

        val remoteResult = remoteDataSource.getCats()
        if (remoteResult is Result.Success) {
            localDataSource.deleteCats()
            localDataSource.insertCats(remoteResult.data.map { it.toCatEntity() })
            emit(remoteResult)
        }
        emit(remoteResult)
    }

    override suspend fun getCatDetails(id: String): Flow<Result<Cat, NetworkError>> = flow {
        val localCatDetails = localDataSource.getCatDetails(id)
        if (localCatDetails != null) {
            emit(Result.Success(localCatDetails.toCat()))
        }

        val remoteResult = remoteDataSource.getCatDetails(id)
        if (remoteResult is Result.Success) {
            localDataSource.insertCat(remoteResult.data.toCatEntity())
            emit(remoteResult)
        }
        emit(remoteResult)
    }
}