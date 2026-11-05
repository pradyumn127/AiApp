package com.example.domain.usecase

import com.example.domain.model.Product
import com.example.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductsByCategoryUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(category: String?): List<Product> {
        return repository.getProducts(category = category, search = null)
    }
}
