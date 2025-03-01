package com.ralphevmanzano.catlibrary.presentation.model

import kotlinx.serialization.Serializable

sealed interface Screen {

    @Serializable
    data object CatList : Screen

    @Serializable
    data class CatDetails(val catId: String, val catName: String) : Screen
}