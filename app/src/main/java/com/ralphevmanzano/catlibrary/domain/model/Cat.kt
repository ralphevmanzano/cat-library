package com.ralphevmanzano.catlibrary.domain.model

data class Cat(
    val id: String,
    val name: String,
    val description: String,
    val weight: String,
    val lifeSpan: String,
    val imageUrl: String,
    val imageHeight: Int,
    val imageWidth: Int
)
