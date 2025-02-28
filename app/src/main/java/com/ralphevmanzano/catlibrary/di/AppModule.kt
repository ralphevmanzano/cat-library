package com.ralphevmanzano.catlibrary.di

import com.ralphevmanzano.catlibrary.data.datasource.CatRemoteDataSource
import com.ralphevmanzano.catlibrary.data.repository.CatRepositoryImpl
import com.ralphevmanzano.catlibrary.domain.repository.CatRepository
import org.koin.dsl.module

val appModule = module {
    single { CatRemoteDataSource(get()) }
    single<CatRepository> { CatRepositoryImpl(get()) }
}