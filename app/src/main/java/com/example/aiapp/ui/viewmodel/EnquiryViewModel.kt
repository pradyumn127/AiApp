package com.example.aiapp.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.domain.model.Enquiry
import com.example.domain.usecase.SubmitEnquiryUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

data class EnquiryUiState(
    val isSubmitting: Boolean = false,
    val isSuccess: Boolean = false,
    val successMessage: String? = null,
    val error: String? = null
)

@HiltViewModel
class EnquiryViewModel @Inject constructor(
    private val submitEnquiryUseCase: SubmitEnquiryUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(EnquiryUiState())
    val uiState: StateFlow<EnquiryUiState> = _uiState.asStateFlow()

    private val emailRegex = Regex("^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,6}$")

    fun submitEnquiry(
        productId: Int,
        productTitle: String,
        name: String,
        email: String,
        message: String
    ) {
        if (name.isBlank() || email.isBlank() || message.isBlank()) {
            _uiState.value = _uiState.value.copy(error = "All fields (Name, Email, Message) are mandatory.")
            return
        }

        if (!emailRegex.matches(email.trim())) {
            _uiState.value = _uiState.value.copy(error = "Please enter a valid email address.")
            return
        }

        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isSubmitting = true, error = null)
            try {
                val enquiry = Enquiry(
                    productId = productId,
                    productTitle = productTitle,
                    customerName = name.trim(),
                    customerEmail = email.trim(),
                    message = message.trim()
                )
                val result = submitEnquiryUseCase(enquiry)
                if (result.success) {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        isSuccess = true,
                        successMessage = "Thank you! Your enquiry has been sent successfully."
                    )
                } else {
                    _uiState.value = _uiState.value.copy(
                        isSubmitting = false,
                        error = result.message
                    )
                }
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isSubmitting = false,
                    error = e.localizedMessage ?: "Failed to submit enquiry"
                )
            }
        }
    }
}
