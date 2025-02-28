package com.ralphevmanzano.catlibrary.presentation.model

import com.ralphevmanzano.catlibrary.domain.model.Cat

data class CatUi(
    val id: String,
    val name: String,
    val description: String,
    val weightFormatted: String,
    val lifeSpanFormatted: String,
    val imageUrl: String,
    val imageHeight: Int,
    val imageWidth: Int
)

fun Cat.toUi(): CatUi {
    return CatUi(
        id = id,
        name = name,
        description = description,
        weightFormatted = "$weight kg",
        lifeSpanFormatted = "$lifeSpan yrs",
        imageUrl = imageUrl,
        imageHeight = imageHeight,
        imageWidth = imageWidth
    )
}