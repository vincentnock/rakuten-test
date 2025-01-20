package com.priceminister.rakutentest.network

import com.priceminister.rakutentest.models.ProductDetails
import com.priceminister.rakutentest.models.ProductListResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface RakutenApi {
    @GET("products/search")
    suspend fun searchProducts(@Query("keyword") keyword: String): ProductListResponse

    @GET("/products/details")
    suspend fun getProductDetails(@Query("id") productId: Long): ProductDetails
}