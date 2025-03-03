package com.ralphevmanzano.catlibrary.presentation.model

import androidx.compose.runtime.Immutable
import com.ralphevmanzano.catlibrary.domain.model.Cat

@Immutable
data class CatCardUi(
    val id: String,
    val name: String,
    val imageUrl: String,
    val imageAspectRatio: Float = 1f,
)

fun Cat.toCatUi(): CatCardUi {
    return CatCardUi(
        id = id,
        name = name,
        imageUrl = imageUrl,
        imageAspectRatio = imageWidth.toFloat() / imageHeight.toFloat()
    )
}