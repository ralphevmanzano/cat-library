package com.ralphevmanzano.catlibrary.di

import android.app.Application
import androidx.room.Room
import com.ralphevmanzano.catlibrary.data.local.CatsDatabase
import org.koin.dsl.module

fun provideCatsDatabase(application: Application): CatsDatabase {
    return Room.databaseBuilder(application, CatsDatabase::class.java, "table_cats")
        .fallbackToDestructiveMigration()
        .build()
}

fun provideCatsDao(catsDatabase: CatsDatabase) = catsDatabase.catDao()

val databaseModule = module {
    single { provideCatsDatabase(get()) }
    single { provideCatsDao(get()) }
}