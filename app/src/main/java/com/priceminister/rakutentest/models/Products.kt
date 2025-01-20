package com.priceminister.rakutentest.models

data class ProductListResponse(
    val totalResultProductsCount: Int,
    val resultProductsCount: Int,
    val pageNumber: Int,
    val title: String,
    val maxProductsPerPage: Int,
    val maxPageNumber: Int,
    val products: List<Product>
)

data class Product(
    val id: Long,
    val newBestPrice: Double?,
    val usedBestPrice: Double?,
    val headline: String,
    val reviewsAverageNote: Double?,
    val nbReviews: Int?,
    val categoryRef: Int,
    val imagesUrls: List<String>,
    val buybox: Buybox?
)

data class Buybox(
    val salePrice: Double?,
    val advertType: String?,
    val advertQuality: String?,
    val saleCrossedPrice: Double?,
    val salePercentDiscount: Int?,
    val isRefurbished: Boolean?
)