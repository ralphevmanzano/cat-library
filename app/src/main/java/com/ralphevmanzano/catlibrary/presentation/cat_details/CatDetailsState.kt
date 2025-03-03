package com.ralphevmanzano.catlibrary.presentation.cat_details

import com.ralphevmanzano.catlibrary.presentation.model.CatDetailsUi

data class CatDetailsState(
    val isLoading: Boolean = false,
    val catDetails: CatDetailsUi? = null,
)