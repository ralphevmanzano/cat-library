package com.ralphevmanzano.catlibrary.data.remote.dto

import com.ralphevmanzano.catlibrary.domain.model.Cat
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatDto(
    val id: String,
    @SerialName("life_span")
    val lifeSpan: String,
    val name: String,
    val description: String,
    val vocalisation: Int,
    val weight: Weight,
    val image: Image? = null,
    @SerialName("reference_image_id")
    val referenceImageId: String? = null
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