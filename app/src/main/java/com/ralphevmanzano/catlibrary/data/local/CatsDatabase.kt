package com.ralphevmanzano.catlibrary.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ralphevmanzano.catlibrary.data.local.entity.CatEntity

@Database(entities = [CatEntity::class], version = 1, exportSchema = false)
abstract class CatsDatabase: RoomDatabase() {
    abstract fun catDao(): CatsDao
}