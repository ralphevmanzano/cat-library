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
        imageUrl = image.url
    )
}