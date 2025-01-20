package com.priceminister.rakutentest.models

// ... other data models

data class ProductDetails(
    val productId: Long,
    val salePrice: Double?,
    val newBestPrice: Double?,
    val usedBestPrice: Double?,
    val seller: Seller?,
    val quality: String?,
    val type: String?,
    val sellerComment: String?,
    val headline: String,
    val description: String,
    val categories: List<String>,
    val globalRating: GlobalRating?,
    val images: List<Image>
)

data class Seller(
    val id: Long,
    val login: String
)

data class GlobalRating(
    val score: Float,
    val nbReviews: Int
)

data class Image(
    val imagesUrls: ImagesUrls,
    val id: Long
)

data class ImagesUrls(
    val entry: List<Entry>
)

data class Entry(
    val size: String,
    val url: String
)