package com.example.domain.usecase

import com.example.domain.model.Enquiry
import com.example.domain.model.EnquiryResult
import com.example.domain.repository.ProductRepository
import javax.inject.Inject

class SubmitEnquiryUseCase @Inject constructor(
    private val repository: ProductRepository
) {
    suspend operator fun invoke(enquiry: Enquiry): EnquiryResult {
        return repository.submitEnquiry(enquiry)
    }
}
