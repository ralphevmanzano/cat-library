package com.ralphevmanzano.catlibrary.data.remote.dto

import kotlinx.serialization.Serializable

@Serializable
data class CatDto(
    val id: String,
    val name: String,
    val image: Image? = null,
)

@Serializable
data class Image(
    val height: Int,
    val id: String,
    val url: String,
    val width: Int
)