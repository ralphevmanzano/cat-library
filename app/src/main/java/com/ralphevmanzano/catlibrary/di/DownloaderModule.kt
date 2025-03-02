package com.ralphevmanzano.catlibrary.di

import androidx.work.WorkManager
import com.ralphevmanzano.catlibrary.data.worker.ImageDownloadWorker
import com.ralphevmanzano.catlibrary.data.repository.ImageDownloaderRepositoryImpl
import com.ralphevmanzano.catlibrary.domain.repository.ImageDownloaderRepository
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val downloaderModule = module {
    single { WorkManager.getInstance(get()) }

    factory<ImageDownloaderRepository> { ImageDownloaderRepositoryImpl(get(), get()) }

    worker { ImageDownloadWorker(get(), get(), get()) }
}