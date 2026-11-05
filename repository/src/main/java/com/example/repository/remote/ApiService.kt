package com.example.repository.remote

import com.example.repository.model.ProductDto
import com.example.repository.model.EnquiryRequestDto
import com.example.repository.model.EnquiryResponseDto
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiService {
    @GET("products/featured")
    suspend fun getFeaturedProducts(): List<ProductDto>

    @GET("products/categories")
    suspend fun getCategories(): List<String>

    @GET("products")
    suspend fun getProducts(
        @Query("category") category: String?,
        @Query("search") search: String?
    ): List<ProductDto>

    @GET("products/{id}")
    suspend fun getProductById(@Path("id") id: Int): ProductDto

    @POST("enquiries")
    suspend fun submitEnquiry(@Body enquiry: EnquiryRequestDto): EnquiryResponseDto
}
