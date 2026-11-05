package com.example.aiapp.ui.viewmodel

import com.example.domain.model.Product
import com.example.domain.model.Enquiry
import com.example.domain.model.EnquiryResult
import com.example.domain.repository.ProductRepository
import com.example.domain.usecase.GetCategoriesUseCase
import com.example.domain.usecase.GetFeaturedProductsUseCase
import com.example.domain.usecase.SearchProductsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.*
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class HomeViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private class FakeRepository : ProductRepository {
        var shouldThrow = false
        val mockProducts = listOf(
            Product(1, "Test Product", 10.0, "Desc", "electronics", "img", 4.8, 100)
        )
        val mockCategories = listOf("electronics", "jewelery")

        override suspend fun getFeaturedProducts(): List<Product> {
            if (shouldThrow) throw RuntimeException("Network Error")
            return mockProducts
        }

        override suspend fun getCategories(): List<String> {
            if (shouldThrow) throw RuntimeException("Network Error")
            return mockCategories
        }

        override suspend fun getProducts(category: String?, search: String?): List<Product> {
            return mockProducts
        }

        override suspend fun getProductById(id: Int): Product = mockProducts[0]

        override suspend fun submitEnquiry(enquiry: Enquiry): EnquiryResult =
            EnquiryResult(true, "Success", 1L)
    }

    private lateinit var fakeRepo: FakeRepository
    private lateinit var viewModel: HomeViewModel

    @Before
    fun setUp() {
        Dispatchers.setMain(testDispatcher)
        fakeRepo = FakeRepository()
        viewModel = HomeViewModel(
            GetFeaturedProductsUseCase(fakeRepo),
            GetCategoriesUseCase(fakeRepo),
            SearchProductsUseCase(fakeRepo)
        )
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun loadHomeData_success() = runTest {
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertEquals(1, state.featuredProducts.size)
        assertEquals(2, state.categories.size)
        assertEquals(null, state.error)
    }

    @Test
    fun loadHomeData_error() = runTest {
        fakeRepo.shouldThrow = true
        viewModel.loadHomeData()
        advanceUntilIdle()
        val state = viewModel.uiState.value
        assertEquals(false, state.isLoading)
        assertEquals("Network Error", state.error)
    }
}
