package com.example.domain.usecase

import com.example.domain.model.Product
import com.example.domain.repository.ProductRepository
import javax.inject.Inject

class GetFeaturedProductsUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(): List<Product> {
        return repository.getFeaturedProducts()
    }
}
