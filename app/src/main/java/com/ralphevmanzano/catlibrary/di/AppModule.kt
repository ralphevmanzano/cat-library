package com.ralphevmanzano.catlibrary.di

import com.ralphevmanzano.catlibrary.data.repository.CatRepositoryImpl
import com.ralphevmanzano.catlibrary.data.repository.datasource.CatLocalDataSource
import com.ralphevmanzano.catlibrary.data.repository.datasource.CatRemoteDataSource
import com.ralphevmanzano.catlibrary.domain.repository.CatRepository
import com.ralphevmanzano.catlibrary.domain.usecase.DownloadImageUseCase
import com.ralphevmanzano.catlibrary.domain.usecase.GetCatDetailsUseCase
import com.ralphevmanzano.catlibrary.domain.usecase.GetCatsUseCase
import com.ralphevmanzano.catlibrary.presentation.cat_details.CatDetailsViewModel
import com.ralphevmanzano.catlibrary.presentation.cat_list.CatListViewModel
import org.koin.core.module.dsl.viewModelOf
import org.koin.dsl.module

val appModule = module {
    single { CatRemoteDataSource(get()) }
    single { CatLocalDataSource(get()) }

    single<CatRepository> { CatRepositoryImpl(get(), get()) }

    single { GetCatsUseCase(get()) }
    single { GetCatDetailsUseCase(get()) }
    single { DownloadImageUseCase(get()) }

    viewModelOf(::CatListViewModel)
    viewModelOf(::CatDetailsViewModel)
}