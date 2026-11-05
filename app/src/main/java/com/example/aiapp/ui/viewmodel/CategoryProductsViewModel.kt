package com.example.aiapp.ui.viewmodel

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Product
import com.example.domain.usecase.GetProductsByCategoryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class CategoryProductsUiState(
    val isLoading: Boolean = false,
    val categoryName: String = "",
    val topRatedProducts: List<Product> = emptyList(),
    val additionalProducts: List<Product> = emptyList(),
    val error: String? = null
)

@HiltViewModel
class CategoryProductsViewModel @Inject constructor(
    private val getProductsByCategoryUseCase: GetProductsByCategoryUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val categoryName: String = savedStateHandle.get<String>("categoryName") ?: ""

    private val _uiState = MutableStateFlow(CategoryProductsUiState(categoryName = categoryName))
    val uiState: StateFlow<CategoryProductsUiState> = _uiState.asStateFlow()

    init {
        loadProducts()
    }

    fun loadProducts() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            try {
                val products = getProductsByCategoryUseCase(categoryName)
                val sorted = products.sortedByDescending { it.ratingRate }
                val topRated = sorted.take(3)
                val additional = sorted.drop(3)
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    topRatedProducts = topRated,
                    additionalProducts = additional
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = e.localizedMessage ?: "Failed to load products"
                )
            }
        }
    }
}
