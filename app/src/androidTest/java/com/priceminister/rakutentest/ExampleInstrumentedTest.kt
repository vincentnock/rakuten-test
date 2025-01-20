package com.priceminister.rakutentest

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithText
import com.priceminister.rakutentest.composable.ProductDetailsScreen
import com.priceminister.rakutentest.models.Entry
import com.priceminister.rakutentest.models.GlobalRating
import com.priceminister.rakutentest.models.Image
import com.priceminister.rakutentest.models.ImagesUrls
import com.priceminister.rakutentest.models.ProductDetails
import com.priceminister.rakutentest.models.Seller
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@HiltAndroidTest
class ProductDetailsScreenTest {


    @get:Rule(order = 0)
    var hiltRule = HiltAndroidRule(this)

    @get:Rule(order = 1)
    val composeTestRule = createComposeRule()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun productDetailsScreen_displaysHeadline() {
        // Arrange
        val productDetails = ProductDetails(
            productId = 123,
            salePrice = 100.0,
            newBestPrice = 200.0,
            usedBestPrice = 200.0,
            seller = Seller(login = "testSeller", id = 100),
            quality = "good",
            type = "",
            sellerComment = "seller comment",
            headline = "headline",
            description = "description",
            categories = listOf("category"),
            globalRating = GlobalRating(4.5f, 30),
            images = listOf(Image(imagesUrls = ImagesUrls(listOf(Entry("", ""))), id = 1000)),
        )

        // Act
        composeTestRule.setContent {
            ProductDetailsScreen(productDetails, false, null, onNewSearch = {})
        }

        // Assert
        composeTestRule.onNodeWithText(productDetails.headline).assertIsDisplayed()
    }
}