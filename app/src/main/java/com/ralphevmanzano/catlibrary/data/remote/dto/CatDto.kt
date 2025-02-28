package com.ralphevmanzano.catlibrary.data.remote.dto

import com.ralphevmanzano.catlibrary.domain.model.Cat
import kotlinx.serialization.Serializable

@Serializable
data class CatDto(
    val id: String,
    val lifeSpan: String,
    val name: String,
    val description: String,
    val vocalisation: Int,
    val weight: Weight,
    val image: Image
)

@Serializable
data class Weight(
    val imperial: String,
    val metric: String
)

@Serializable
data class Image(
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)