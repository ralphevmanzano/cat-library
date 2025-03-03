package com.ralphevmanzano.catlibrary.presentation.model

import androidx.compose.runtime.Immutable
import com.ralphevmanzano.catlibrary.domain.model.Cat

@Immutable
data class CatDetailsUi(
    val id: String,
    val name: String,
    val imageUrl: String,
    val description: String,
    val weightFormatted: String,
    val lifeSpanFormatted: String,
)

fun Cat.toCatDetailsUi(): CatDetailsUi {
    return CatDetailsUi(
        id = id,
        name = name,
        imageUrl = imageUrl,
        description = description,
        weightFormatted = "$weight kg",
        lifeSpanFormatted = "$lifeSpan years",
    )
}
