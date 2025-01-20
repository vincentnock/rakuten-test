package com.priceminister.rakutentest.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.priceminister.rakutentest.network.RakutenApi
import com.priceminister.rakutentest.models.Product
import com.priceminister.rakutentest.models.ProductDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductViewModel @Inject constructor(
    private val rakutenApi: RakutenApi
) : ViewModel() {

    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading

    private val _isLoadingDetails = MutableStateFlow(false)
    val isLoadingDetails: StateFlow<Boolean> = _isLoadingDetails

    private val _error = MutableStateFlow<String?>(null)
    val error: StateFlow<String?> = _error

    private val _products = MutableStateFlow<List<Product>>(emptyList())
    val products: StateFlow<List<Product>> = _products

    private val _productDetails = MutableStateFlow<ProductDetails?>(null)
    val productDetails: StateFlow<ProductDetails?> = _productDetails

    var currentSearch: String by mutableStateOf("")

    init {
        searchProducts("samsung")
    }

    fun searchProducts(keyword: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _error.value = null

            try {
                val response = rakutenApi.searchProducts(keyword)
                _products.value = response.products
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoading.value = false
            }

        }
    }

    fun getProductDetails(productId: Long) {
        _productDetails.value = null
        _isLoadingDetails.value = true
        _error.value = null
        viewModelScope.launch {
            try {
                _productDetails.value = rakutenApi.getProductDetails(productId)
            } catch (e: Exception) {
                _error.value = e.message
            } finally {
                _isLoadingDetails.value = false
            }
        }
    }
}