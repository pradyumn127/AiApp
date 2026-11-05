package com.example.domain.model

data class Enquiry(
    val productId: Int,
    val productTitle: String,
    val customerName: String,
    val customerEmail: String,
    val message: String
)

data class EnquiryResult(
    val success: Boolean,
    val message: String,
    val enquiryId: Long?
)
