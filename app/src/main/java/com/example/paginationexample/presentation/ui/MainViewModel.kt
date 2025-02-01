package com.example.paginationexample.presentation.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.Pager
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.paginationexample.domain.model.Image
import com.example.paginationexample.domain.repositroy.ImageRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.update

data class UiState(
    val images: List<Image> = emptyList(),
    val isLoading: Boolean = false,
    val query: String = "Yellow flowers"
)

sealed interface UiEvent {
    data class OnQueryChange(val query: String) : UiEvent
}

class MainViewModel(private val repository: ImageRepository) : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState = _uiState.asStateFlow()

    @OptIn(FlowPreview::class, ExperimentalCoroutinesApi::class)
    val images: Flow<PagingData<Image>> = uiState.filter { it.query.isNotBlank() }.debounce(1000)
        .flatMapLatest { uiState -> repository.getImages(uiState.query).flow.cachedIn(viewModelScope) }

    private fun onQueryChange(query: String) {
        _uiState.update {
            it.copy(query = query)
        }
    }

    fun onEvent(uiEvent: UiEvent) {
        when (uiEvent) {

            is UiEvent.OnQueryChange -> {
                onQueryChange(uiEvent.query)
            }
        }
    }
}