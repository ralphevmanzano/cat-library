package com.ralphevmanzano.catlibrary.presentation.model

import androidx.compose.runtime.Immutable
import com.ralphevmanzano.catlibrary.domain.model.Cat

@Immutable
data class CatUi(
    val id: String,
    val name: String,
    val description: String,
    val weightFormatted: String,
    val lifeSpanFormatted: String,
    val imageUrl: String,
    val imageAspectRatio: Float = 1f,
)

fun Cat.toCatUi(): CatUi {
    return CatUi(
        id = id,
        name = name,
        description = description,
        weightFormatted = "$weight kg",
        lifeSpanFormatted = "$lifeSpan yrs",
        imageUrl = imageUrl,
        imageAspectRatio = imageWidth.toFloat() / imageHeight.toFloat()
    )
}