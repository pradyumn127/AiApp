package com.example.aiapp.ui.viewmodel

import com.example.domain.model.Enquiry
import com.example.domain.model.EnquiryResult
import com.example.domain.model.Product
import com.example.domain.repository.ProductRepository
import com.example.domain.usecase.SubmitEnquiryUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class EnquiryViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private class FakeRepository : ProductRepository {
        var shouldFail = false
        override suspend fun getFeaturedProducts() = emptyList<Product>()
        override suspend fun getCategories() = emptyList<String>()
        override suspend fun getProducts(category: String?, search: String?) = emptyList<Product>()
        override suspend fun getProductById(id: Int) = throw NotImplementedError()
        override suspend fun submitEnquiry(enquiry: Enquiry): EnquiryResult {
            if (shouldFail) return EnquiryResult(false, "Failed", null)
            return EnquiryResult(true, "Success", 123L)
        }
    }

    private lateinit var fakeRepo: FakeRepository
    private lateinit var viewModel: EnquiryViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeRepository()
        viewModel = EnquiryViewModel(SubmitEnquiryUseCase(fakeRepo))
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun submitEnquiry_blankFields_showsError() = runTest {
        viewModel.submitEnquiry(1, "Product", "", "", "")
        val state = viewModel.uiState.value
        assertEquals("All fields (Name, Email, Message) are mandatory.", state.error)
    }

    @Test
    fun submitEnquiry_invalidEmail_showsError() = runTest {
        viewModel.submitEnquiry(1, "Product", "John Doe", "invalid-email", "Hello")
        val state = viewModel.uiState.value
        assertEquals("Please enter a valid email address.", state.error)
    }

    @Test
    fun submitEnquiry_validInput_success() = runTest {
        viewModel.submitEnquiry(1, "Product", "John Doe", "john@example.com", "I am interested.")
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertEquals(true, state.isSuccess)
        assertEquals(null, state.error)
    }
}
