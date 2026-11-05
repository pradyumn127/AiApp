package com.example.repository.model

import com.example.domain.model.Product
import com.google.gson.annotations.SerializedName

data class RatingDto(
    @SerializedName("rate") val rate: Double?,
    @SerializedName("count") val count: Int?
)

data class ProductDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("price") val price: Double,
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("image") val image: String,
    @SerializedName("rating") val rating: RatingDto?
) {
    fun toDomain(): Product {
        return Product(
            id = id,
            title = title,
            price = price,
            description = description,
            category = category,
            image = image,
            ratingRate = rating?.rate ?: 0.0,
            ratingCount = rating?.count ?: 0
        )
    }
}
