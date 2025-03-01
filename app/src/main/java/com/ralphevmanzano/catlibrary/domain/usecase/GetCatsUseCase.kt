package com.ralphevmanzano.catlibrary.domain.usecase

import com.ralphevmanzano.catlibrary.domain.model.networking.map
import com.ralphevmanzano.catlibrary.domain.repository.CatRepository
import com.ralphevmanzano.catlibrary.presentation.model.toCatUi

class GetCatsUseCase(private val catRepository: CatRepository) {
    suspend operator fun invoke() = catRepository.getCats().map { cats ->
        cats.map { it.toCatUi() }
    }
}