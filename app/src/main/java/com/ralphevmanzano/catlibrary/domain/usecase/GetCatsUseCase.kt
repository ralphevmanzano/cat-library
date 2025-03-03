package com.ralphevmanzano.catlibrary.domain.usecase

import com.ralphevmanzano.catlibrary.domain.model.networking.map
import com.ralphevmanzano.catlibrary.domain.repository.CatRepository
import com.ralphevmanzano.catlibrary.presentation.model.toCatUi
import kotlinx.coroutines.flow.map

class GetCatsUseCase(private val catRepository: CatRepository) {
    suspend operator fun invoke(isRefresh: Boolean) = catRepository.getCats(isRefresh).map { result ->
        result.map { cats ->
            cats.map { it.toCatUi() }.sortedBy { it.name }
        }
    }
}