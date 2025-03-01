package com.ralphevmanzano.catlibrary.presentation.cat_list

import androidx.compose.runtime.Immutable
import com.ralphevmanzano.catlibrary.presentation.model.CatUi

@Immutable
data class CatListState(
    val isLoading: Boolean = false,
    val cats: List<CatUi> = emptyList(),
    val error: String? = null,
)
