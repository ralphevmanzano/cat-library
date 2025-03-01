package com.ralphevmanzano.catlibrary.data.repository.datasource

import com.ralphevmanzano.catlibrary.data.mappers.toCat
import com.ralphevmanzano.catlibrary.data.remote.CatService
import com.ralphevmanzano.catlibrary.domain.model.Cat
import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.Result
import com.ralphevmanzano.catlibrary.domain.model.networking.map
import com.ralphevmanzano.catlibrary.utils.safeCall

class CatRemoteDataSource(private val catService: CatService) {

    suspend fun getCats(): Result<List<Cat>, NetworkError> {
        return safeCall { catService.getCats() }.map { response ->
            response.filter { it.image != null }.map { it.toCat() }
        }
    }

    suspend fun getCatDetails(id: String): Result<Cat, NetworkError> {
        return safeCall { catService.getCatDetails(id) }.map { it.toCat() }
    }
}