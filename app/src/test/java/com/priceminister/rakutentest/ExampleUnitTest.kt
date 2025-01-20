package com.priceminister.rakutentest

import androidx.activity.compose.setContent
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
import com.priceminister.rakutentest.network.RakutenApi
import com.priceminister.rakutentest.viewmodel.ProductViewModel
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.runTest
import org.junit.Test

import org.junit.Assert.*
import org.junit.Before
import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import javax.inject.Inject
import org.mockito.Mockito.mock
import org.mockito.junit.MockitoJUnitRunner

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {
    @Test
    fun addition_isCorrect() {
        assertEquals(4, 2 + 2)
    }
}