package com.example.aiapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Product
import com.example.domain.usecase.GetCategoriesUseCase
import com.example.domain.usecase.GetFeaturedProductsUseCase
import com.example.domain.usecase.SearchProductsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class HomeUiState(
    val isLoading: Boolean = false,
    val featuredProducts: List<Product> = emptyList(),
    val allProducts: List<Product> = emptyList(),
    val categories: List<String> = emptyList(),
    val searchResults: List<Product> = emptyList(),
    val searchQuery: String = "",
    val activeSearchQuery: String = "",
    val isSearching: Boolean = false,
    val error: String? = null
)

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getFeaturedProductsUseCase: GetFeaturedProductsUseCase,
    private val getCategoriesUseCase: GetCategoriesUseCase,
    private val searchProductsUseCase: SearchProductsUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        loadHomeData()
    }

    fun loadHomeData() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val featured = getFeaturedProductsUseCase()
                val categories = getCategoriesUseCase()
                val all = searchProductsUseCase("")
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    featuredProducts = featured,
                    allProducts = all,
                    categories = categories
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Unknown error occurred"
                )
            }
        }
    }

    fun onSearchQueryChanged(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
        if (query.isBlank()) {
            _uiState.value = _uiState.value.copy(searchResults = emptyList(), activeSearchQuery = "", isSearching = false)
        }
    }

    fun performSearch() {
        val query = _uiState.value.searchQuery.trim()
        if (query.isBlank()) {
            clearSearch()
            return
        }
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSearching = true, activeSearchQuery = query)
            try {
                val results = searchProductsUseCase(query)
                _uiState.value = _uiState.value.copy(
                    searchResults = results,
                    isSearching = false
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSearching = false,
                    error = e.localizedMessage
                )
            }
        }
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            activeSearchQuery = "",
            searchResults = emptyList(),
            isSearching = false
        )
    }
}
