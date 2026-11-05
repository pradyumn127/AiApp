package com.example.domain.usecase

import com.example.domain.model.Product
import com.example.domain.repository.ProductRepository
import javax.inject.Inject

class SearchProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(query: String): List<Product> {
        return repository.getProducts(category = null, search = query)
    }
}
