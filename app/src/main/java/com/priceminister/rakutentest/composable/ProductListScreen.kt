package com.priceminister.rakutentest.composable

import android.util.Log
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import coil.compose.AsyncImage
import com.priceminister.rakutentest.composable.ui.roundToPx
import com.priceminister.rakutentest.composable.ui.toIntPx
import com.priceminister.rakutentest.models.Product
import com.priceminister.rakutentest.ui.theme.Red
import com.priceminister.rakutentest.viewmodel.ProductViewModel
import java.text.NumberFormat
import kotlinx.coroutines.delay

@Composable
fun ProductListScreen(navController: NavHostController, viewModel: ProductViewModel) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()

    val focusManager = LocalFocusManager.current
    var fullTitleState by remember { mutableStateOf(FullTitleState()) }

    Box(modifier = Modifier.fillMaxSize()) {

        Column(modifier = Modifier.fillMaxSize()) {

            Box(modifier = Modifier
                .background(Color.White)
                .padding(bottom = 10.dp)) {
                OutlinedTextField(
                    value = viewModel.currentSearch,
                    onValueChange = { viewModel.currentSearch = it },
                    label = { Text("Rechercher sur Rakuten") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                    keyboardActions = KeyboardActions(
                        onSearch = {
                            viewModel.searchProducts(viewModel.currentSearch)
                            // hide the keyboard
                            focusManager.clearFocus()
                        }
                    ),
                    trailingIcon = {
                        if (viewModel.currentSearch.isNotEmpty()) {
                            IconButton(onClick = { viewModel.currentSearch = "" }) {
                                Icon(
                                    imageVector = Icons.Filled.Clear,
                                    contentDescription = "Clear"
                                )
                            }
                        }
                    }
                )
            }

            Spacer(Modifier.height(16.dp))

            if (isLoading) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator()
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(products) { product ->
                        ProductListItem(
                            product = product,
                            onItemClick = { navController.navigate("product_details/${product.id}") },
                            onLongClick = { offset ->
                                fullTitleState = FullTitleState(true, offset, product)
                            }
                        )
                    }
                }
            }
        }

        // Overlay for the bubble
        if (fullTitleState.show && fullTitleState.product != null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
            ) {

                FullTitleBubble(
                    fullTitleState = fullTitleState,
                    onDismiss = { fullTitleState = fullTitleState.copy(show = false) }
                )
            }
        }
    }
}

@Composable
fun ProductListItem(product: Product, onItemClick: () -> Unit, onLongClick: (Offset) -> Unit) {

    var itemPosition by remember { mutableStateOf(Offset.Zero) }
    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(8.dp)
            .background(Color.White, RoundedCornerShape(8.dp))
            .padding(8.dp)
            .onGloballyPositioned { coordinates ->
                itemPosition = coordinates.positionInRoot()
            }
            .pointerInput(Unit) {
                detectTapGestures(
                    onLongPress = { offset ->
                        onLongClick(itemPosition + offset)
                    },
                    onTap = {
                        onItemClick()
                    }
                )
            }
    ) {
        AsyncImage(
            model = product.imagesUrls.firstOrNull(),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f) // Maintain aspect ratio for image
                .clip(RoundedCornerShape(8.dp)),
            contentScale = ContentScale.Crop
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = product.headline,
            style = MaterialTheme.typography.bodyMedium,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(10.dp))

        // Prices
        product.newBestPrice?.let { newPrice ->
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = "Neuf dès ", style = MaterialTheme.typography.bodyMedium, color = Red)
                Text(
                    text = formatPrice(newPrice),
                    style = MaterialTheme.typography.bodyLarge,
                    color = Red,
                    fontWeight = FontWeight.Bold
                )
            }
        }
        product.usedBestPrice?.let { usedPrice ->
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = "Occasion dès ", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = formatPrice(usedPrice),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

data class FullTitleState(
    val show: Boolean = false,
    val position: Offset = Offset.Zero,
    val product: Product? = null
)

@Composable
fun FullTitleBubble(fullTitleState: FullTitleState, onDismiss: () -> Unit) {
    val view = LocalView.current
    var bubbleSize by remember { mutableStateOf(IntSize.Zero) }

    LaunchedEffect(key1 = fullTitleState.show) {
        delay(2000) // Hide bubble after 2 seconds
        onDismiss()
    }

    val bubbleX = if (fullTitleState.position.x < view.width / 2) {
        20.dp.roundToPx() // Align to left
    } else {
        view.width - bubbleSize.width - 20.dp.toIntPx() // Align to right, adjust 200 for bubble width
    }

    Box(
        modifier = Modifier
            .fillMaxWidth(0.7f)
            .onGloballyPositioned { coordinates ->
                bubbleSize = coordinates.size
            }
            .offset {
                IntOffset(
                    bubbleX,
                    fullTitleState.position.y.toInt() - bubbleSize.height - 80.dp.roundToPx() // Adjust for bubble height and 10dp spacing
                )
            }
            .background(Color.LightGray, RoundedCornerShape(8.dp))
            .padding(8.dp)
    ) {
        Text(text = fullTitleState.product!!.headline, style = MaterialTheme.typography.bodyMedium)
    }
}

@Composable
fun formatPrice(price: Double): String {
    val format = NumberFormat.getCurrencyInstance()
    format.currency = java.util.Currency.getInstance("EUR")
    return format.format(price)
}