package com.ralphevmanzano.catlibrary.presentation.model

import com.ralphevmanzano.catlibrary.domain.model.Cat
import kotlinx.serialization.Serializable

@Serializable
data class CatUi(
    val id: String,
    val name: String,
    val description: String,
    val weightFormatted: String,
    val lifeSpanFormatted: String,
    val imageUrl: String,
    val imageWidth: Int,
    val imageHeight: Int
)

fun Cat.toCatUi(): CatUi {
    return CatUi(
        id = id,
        name = name,
        description = description,
        weightFormatted = "$weight kg",
        lifeSpanFormatted = "$lifeSpan yrs",
        imageUrl = imageUrl,
        imageWidth = imageWidth,
        imageHeight = imageHeight,
    )
}