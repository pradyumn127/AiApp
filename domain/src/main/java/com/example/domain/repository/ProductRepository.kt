package com.example.domain.repository

import com.example.domain.model.Product
import com.example.domain.model.Enquiry
import com.example.domain.model.EnquiryResult

interface ProductRepository {
    suspend fun getFeaturedProducts(): List<Product>
    suspend fun getCategories(): List<String>
    suspend fun getProducts(category: String?, search: String?): List<Product>
    suspend fun getProductById(id: Int): Product
    suspend fun submitEnquiry(enquiry: Enquiry): EnquiryResult
}
