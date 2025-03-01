package com.ralphevmanzano.catlibrary.data.mappers

import com.ralphevmanzano.catlibrary.data.remote.dto.CatDto
import com.ralphevmanzano.catlibrary.domain.model.Cat

fun CatDto.toCat(): Cat {
    return Cat(
        id = id,
        name = name,
        description = description,
        weight = weight.metric,
        lifeSpan = lifeSpan,
        imageUrl = image?.url.orEmpty().ifEmpty {
            "https://cdn2.thecatapi.com/images/${referenceImageId}.jpg"
        },
        imageHeight = image?.height ?: 0,
        imageWidth = image?.width ?: 0
    )
}