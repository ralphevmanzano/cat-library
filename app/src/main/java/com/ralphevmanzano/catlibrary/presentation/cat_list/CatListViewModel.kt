package com.ralphevmanzano.catlibrary.presentation.cat_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ralphevmanzano.catlibrary.domain.model.networking.NetworkError
import com.ralphevmanzano.catlibrary.domain.model.networking.onError
import com.ralphevmanzano.catlibrary.domain.model.networking.onSuccess
import com.ralphevmanzano.catlibrary.domain.usecase.GetCatsUseCase
import com.ralphevmanzano.catlibrary.utils.presentation.OnetimeWhileSubscribed
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CatListViewModel(private val getCatsUseCase: GetCatsUseCase) : ViewModel() {

    private val _state = MutableStateFlow(CatListState())
    val state = _state
        .onStart { getCats() }
        .stateIn(
            viewModelScope,
            OnetimeWhileSubscribed(5000),
            CatListState()
        )

    private val _errorEvents = MutableSharedFlow<NetworkError>()
    val errorEvents = _errorEvents.asSharedFlow()

    private fun getCats() {
        viewModelScope.launch {
            _state.update {
                it.copy(isLoading = true)
            }

            getCatsUseCase().collect { result ->
                result.onSuccess { cats ->
                    _state.update {
                        it.copy(isLoading = false, cats = cats)
                    }
                }.onError { error ->
                    _state.update {
                        it.copy(isLoading = false)
                    }
                    _errorEvents.emit(error)
                }
            }
        }
    }
}