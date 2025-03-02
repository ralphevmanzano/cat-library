package com.ralphevmanzano.catlibrary.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.ralphevmanzano.catlibrary.data.local.entity.CatEntity

@Dao
interface CatsDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCats(cats: List<CatEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCat(cat: CatEntity)

    @Query("SELECT * FROM cat")
    suspend fun getCats(): List<CatEntity>

    @Query("SELECT * FROM cat WHERE id = :id")
    suspend fun getCatDetails(id: String): CatEntity?

    @Query("DELETE FROM cat")
    suspend fun deleteAllCats()


}