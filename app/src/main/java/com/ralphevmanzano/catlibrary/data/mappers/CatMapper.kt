package com.ralphevmanzano.catlibrary.data.mappers

import com.ralphevmanzano.catlibrary.data.local.entity.CatEntity
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

fun Cat.toCatEntity(): CatEntity {
    return CatEntity(
        id = id,
        name = name,
        description = description,
        weight = weight,
        lifeSpan = lifeSpan,
        imageUrl = imageUrl,
        imageHeight = imageHeight,
        imageWidth = imageWidth
    )
}

fun CatEntity.toCat(): Cat {
    return Cat(
        id = id,
        name = name,
        description = description,
        weight = weight,
        lifeSpan = lifeSpan,
        imageUrl = imageUrl,
        imageHeight = imageHeight,
        imageWidth = imageWidth
    )
}