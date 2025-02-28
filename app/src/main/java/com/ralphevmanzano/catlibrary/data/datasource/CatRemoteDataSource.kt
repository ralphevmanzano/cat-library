package com.ralphevmanzano.catlibrary.data.datasource

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
            response.map { it.toCat() }
        }
    }
}