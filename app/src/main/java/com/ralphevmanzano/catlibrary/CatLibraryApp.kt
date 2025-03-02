package com.ralphevmanzano.catlibrary

import android.app.Application
import com.ralphevmanzano.catlibrary.di.appModule
import com.ralphevmanzano.catlibrary.di.databaseModule
import com.ralphevmanzano.catlibrary.di.downloaderModule
import com.ralphevmanzano.catlibrary.di.networkModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.androidx.workmanager.koin.workManagerFactory
import org.koin.core.context.startKoin

class CatLibraryApp: Application() {

    override fun onCreate() {
        super.onCreate()
        startKoin {
            androidContext(this@CatLibraryApp)
            androidLogger()

            workManagerFactory()
            modules(appModule, networkModule, downloaderModule, databaseModule)
        }
    }
}