package com.ralphevmanzano.catlibrary.data.remote.dto

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class CatDetailsDto(
    val id: String,
    @SerialName("life_span")
    val lifeSpan: String,
    val name: String,
    val description: String,
    val weight: Weight,
)

@Serializable
data class Weight(
    val imperial: String,
    val metric: String
)