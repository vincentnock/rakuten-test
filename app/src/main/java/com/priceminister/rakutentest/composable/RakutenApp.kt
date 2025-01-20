package com.priceminister.rakutentest.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import coil.compose.AsyncImage
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import com.priceminister.rakutentest.viewmodel.ProductViewModel

@Composable
fun RakutenApp(
    navController: androidx.navigation.NavHostController = rememberNavController(),
    viewModel: ProductViewModel = hiltViewModel()
) {
    // We want to force theme to white, so set the status bar icons to dark
    val systemUiController = rememberSystemUiController()
    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.White,
            darkIcons = true
        )
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = { TopBar() }
    ) { innerPadding ->
        // Navhost to handle navigation between different screens
        NavHost(
            modifier = Modifier.padding(innerPadding) ,
            navController = navController,
            startDestination = "product_list"
        ) {
            composable("product_list") { ProductListScreen(navController, viewModel) }
            composable(
                "product_details/{productId}",
                arguments = listOf(navArgument("productId") { type = NavType.LongType })
            ) { backStackEntry ->
                val productId = backStackEntry.arguments?.getLong("productId") ?: 0L
                LaunchedEffect(productId) {
                    viewModel.getProductDetails(productId)
                }

                val productDetails by viewModel.productDetails.collectAsState()
                val isLoading by viewModel.isLoadingDetails.collectAsState()
                val error by viewModel.error.collectAsState()

                ProductDetailsScreen(productDetails, isLoading, error, onNewSearch = {
                    viewModel.currentSearch = it
                    viewModel.searchProducts(it)
                    navController.navigate("product_list")
                })
            }
        }
    }
}

@Composable
fun TopBar() {
    Box(
        modifier = Modifier
            .background(Color.White)
            .statusBarsPadding()
            .height(48.dp)
            .fillMaxWidth(),
        contentAlignment = androidx.compose.ui.Alignment.Center
    ) {
        AsyncImage(
            model = "https://static.captcha-delivery.com/captcha/assets/set/090d3859ff6840b2280f4708cf08cdaed873d967/logo.png?update_cache=7304033113750590796",
            contentDescription = "Logo",
            modifier = Modifier
                .padding(8.dp)
                .size(100.dp) ,
            contentScale = ContentScale.Fit
        )
    }
}