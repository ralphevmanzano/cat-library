package com.ralphevmanzano.catlibrary.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "cat")
data class CatEntity(
    @PrimaryKey
    val id: String,
    val name: String,
    val description: String,
    val weight: String,
    val lifeSpan: String,
    val imageUrl: String,
    val imageHeight: Int,
    val imageWidth: Int
)