package com.example.repository.repository

import com.example.domain.model.Enquiry
import com.example.domain.model.EnquiryResult
import com.example.domain.model.Product
import com.example.domain.repository.ProductRepository
import com.example.repository.model.EnquiryRequestDto
import com.example.repository.remote.ApiService
import javax.inject.Inject

class ProductRepositoryImpl @Inject constructor(
    private val apiService: ApiService
) : ProductRepository {

    override suspend fun getFeaturedProducts(): List<Product> {
        return apiService.getFeaturedProducts().map { it.toDomain() }
    }

    override suspend fun getCategories(): List<String> {
        return apiService.getCategories()
    }

    override suspend fun getProducts(category: String?, search: String?): List<Product> {
        return apiService.getProducts(category, search).map { it.toDomain() }
    }

    override suspend fun getProductById(id: Int): Product {
        return apiService.getProductById(id).toDomain()
    }

    override suspend fun submitEnquiry(enquiry: Enquiry): EnquiryResult {
        val requestDto = EnquiryRequestDto.fromDomain(enquiry)
        val response = apiService.submitEnquiry(requestDto)
        return EnquiryResult(
            success = response.success,
            message = response.message,
            enquiryId = response.enquiryId
        )
    }
}
