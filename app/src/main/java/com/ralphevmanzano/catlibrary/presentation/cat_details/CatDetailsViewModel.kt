package com.ralphevmanzano.catlibrary.presentation.cat_details

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ralphevmanzano.catlibrary.domain.model.networking.DownloadStatus
import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.onError
import com.ralphevmanzano.catlibrary.domain.model.networking.onSuccess
import com.ralphevmanzano.catlibrary.domain.usecase.DownloadImageUseCase
import com.ralphevmanzano.catlibrary.domain.usecase.GetCatDetailsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatDetailsViewModel(
    savedStateHandle: SavedStateHandle,
    private val getCatDetailsUseCase: GetCatDetailsUseCase,
    private val downloadImageUseCase: DownloadImageUseCase
) : ViewModel() {

    private val catId = savedStateHandle.get<String>("catId") ?: ""
    val catName = savedStateHandle.getStateFlow("catName", "")

    private val _state = MutableStateFlow(CatDetailsState())
    val state = _state
        .onStart { getCatDetails() }
        .stateIn(
            viewModelScope,
            SharingStarted.WhileSubscribed(5000),
            CatDetailsState()
        )

    private val _errorEvents = MutableSharedFlow<NetworkError>()
    val errorEvents = _errorEvents.asSharedFlow()

    private val _downloadStatus = MutableStateFlow<DownloadStatus>(DownloadStatus.Idle)
    val downloadStatus = _downloadStatus.asStateFlow()

    private fun getCatDetails() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            getCatDetailsUseCase(catId)
                .collect { result ->
                    result.onSuccess { cat ->
                        _state.update {
                            it.copy(isLoading = false, cat = cat)
                        }
                    }.onError { error ->
                        _state.update {
                            it.copy(isLoading = false, error = it.error)
                        }
                        _errorEvents.emit(error)
                    }

                }
        }
    }

    fun downloadImage(imageUrl: String) {
        if (imageUrl.isBlank()) return
        viewModelScope.launch {
            val cat = state.value.cat
            val fileName = "${cat?.name ?: "cat"}_${System.currentTimeMillis()}.jpg"

            downloadImageUseCase(imageUrl, fileName)
                .collect {
                    _downloadStatus.value = it
                }
        }
    }
}