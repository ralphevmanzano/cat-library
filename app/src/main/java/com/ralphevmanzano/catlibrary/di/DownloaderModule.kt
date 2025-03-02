package com.ralphevmanzano.catlibrary.di

import androidx.work.WorkManager
import com.ralphevmanzano.catlibrary.data.worker.ImageDownloadWorker
import com.ralphevmanzano.catlibrary.data.worker.ImageDownloaderImpl
import com.ralphevmanzano.catlibrary.domain.repository.ImageDownloader
import org.koin.androidx.workmanager.dsl.worker
import org.koin.dsl.module

val downloaderModule = module {
    single { WorkManager.getInstance(get()) }

    factory<ImageDownloader> { ImageDownloaderImpl(get(), get()) }

    worker { ImageDownloadWorker(get(), get(), get()) }
}