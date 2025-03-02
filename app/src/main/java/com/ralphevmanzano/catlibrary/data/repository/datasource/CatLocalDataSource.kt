package com.ralphevmanzano.catlibrary.data.repository.datasource

import com.ralphevmanzano.catlibrary.data.local.CatsDao
import com.ralphevmanzano.catlibrary.data.local.entity.CatEntity

class CatLocalDataSource(private val catsDao: CatsDao) {
    suspend fun getCats() = catsDao.getCats()
    suspend fun getCatDetails(id: String) = catsDao.getCatDetails(id)
    suspend fun insertCats(cats: List<CatEntity>) = catsDao.insertCats(cats)
    suspend fun insertCat(cat: CatEntity) = catsDao.insertCat(cat)
    suspend fun deleteCats() = catsDao.deleteAllCats()
}