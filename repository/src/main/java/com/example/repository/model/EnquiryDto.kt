package com.example.repository.model

import com.example.domain.model.Enquiry
import com.google.gson.annotations.SerializedName

data class EnquiryRequestDto(
    @SerializedName("productId") val productId: Int,
    @SerializedName("productTitle") val productTitle: String,
    @SerializedName("customerName") val customerName: String,
    @SerializedName("customerEmail") val customerEmail: String,
    @SerializedName("message") val message: String
) {
    companion object {
        fun fromDomain(enquiry: Enquiry): EnquiryRequestDto {
            return EnquiryRequestDto(
                productId = enquiry.productId,
                productTitle = enquiry.productTitle,
                customerName = enquiry.customerName,
                customerEmail = enquiry.customerEmail,
                message = enquiry.message
            )
        }
    }
}

data class EnquiryResponseDto(
    @SerializedName("success") val success: Boolean,
    @SerializedName("message") val message: String,
    @SerializedName("enquiryId") val enquiryId: Long?
)
