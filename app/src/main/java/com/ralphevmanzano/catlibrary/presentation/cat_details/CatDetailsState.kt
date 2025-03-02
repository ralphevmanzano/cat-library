package com.ralphevmanzano.catlibrary.presentation.cat_details

import com.ralphevmanzano.catlibrary.presentation.model.CatUi

data class CatDetailsState(
    val isLoading: Boolean = false,
    val cat: CatUi? = null,
)