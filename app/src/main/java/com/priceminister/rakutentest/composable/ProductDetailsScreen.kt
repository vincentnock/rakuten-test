package com.priceminister.rakutentest.composable

import android.text.style.URLSpan
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.ClickableText
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.positionInParent
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.LinkAnnotation
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.core.text.HtmlCompat

import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.priceminister.rakutentest.composable.ui.RatingStar
import com.priceminister.rakutentest.models.GlobalRating
import com.priceminister.rakutentest.models.ProductDetails
import com.priceminister.rakutentest.ui.theme.Blue
import com.priceminister.rakutentest.ui.theme.Red
import com.priceminister.rakutentest.viewmodel.ProductViewModel
import kotlin.text.append

@Composable
fun ProductDetailsScreen(
    productDetails: ProductDetails?,
    isLoading: Boolean,
    error: String?,
    onNewSearch: (String) -> Unit
) {
    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
    } else if (error != null) {
        Text("Error: $error")
    } else {
        productDetails?.let { details ->
            ProductDetailsContent(details, onNewSearch)
        }
    }
}

@Composable
fun ProductDetailsContent(details: ProductDetails, onNewSearch: (String) -> Unit) {
    val pagerState = rememberPagerState(pageCount = { details.images.size })

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        // Product images pager
        HorizontalPager(
            state = pagerState,
            modifier = Modifier
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .padding(20.dp)
        ) { page ->
            AsyncImage(
                model = details.images[page].imagesUrls.entry.firstOrNull { it.size == "ORIGINAL" }?.url,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.FillHeight
            )
        }

        // Pager indicator
        Row(
            Modifier
                .wrapContentHeight()
                .fillMaxWidth()
                .background(Color.White, RoundedCornerShape(8.dp))
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(pagerState.pageCount) { iteration ->
                val color = if (pagerState.currentPage == iteration) Color.DarkGray else Color.LightGray
                Box(
                    modifier = Modifier
                        .padding(2.dp)
                        .clip(CircleShape)
                        .background(color)
                        .size(6.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = details.headline,
            style = MaterialTheme.typography.headlineSmall.copy(
                fontWeight = FontWeight.Bold
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Rating and Reviews (if available)
        details.globalRating?.let { Rating(it) }

        // Prices Information
        details.newBestPrice?.let { newPrice ->
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
        details.usedBestPrice?.let { usedPrice ->
            Row(verticalAlignment = Alignment.Bottom) {
                Text(text = "Occasion dès ", style = MaterialTheme.typography.bodyMedium)
                Text(
                    text = formatPrice(usedPrice),
                    style = MaterialTheme.typography.bodyLarge,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            buildAnnotatedString {
                append("Vendu par ")
                withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                    append(details.seller?.login ?: "")
                }
            },
            style = MaterialTheme.typography.bodyMedium
        )

        details.sellerComment?.let {
            Text(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .background(Color.White, RoundedCornerShape(5.dp))
                    .padding(8.dp),
                text = details.sellerComment,
                color = Color(0xFF929292),
                style = MaterialTheme.typography.bodySmall
            )
        }


        Spacer(modifier = Modifier.height(16.dp))

        // Product Description (if available)
        details.description.let { description ->
            HtmlText(description, onNewSearch)
            Spacer(modifier = Modifier.height(16.dp))
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun Rating(rating: GlobalRating) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        RatingStar(rating = rating.score)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "(${rating.nbReviews} avis)",
            style = MaterialTheme.typography.bodyMedium
        )
    }
}

@Composable
fun HtmlText(description: String, onNewSearch: (String) -> Unit) {
    val descriptionHtml = HtmlCompat.fromHtml(description, HtmlCompat.FROM_HTML_MODE_COMPACT)
    val uriHandler = LocalUriHandler.current

    val annotatedString = buildAnnotatedString {
        append(descriptionHtml.toString())
        // Add URLSpans to the AnnotatedString
        descriptionHtml.getSpans(0, descriptionHtml.length, URLSpan::class.java).forEach { urlSpan ->
            val start = descriptionHtml.getSpanStart(urlSpan)
            val end = descriptionHtml.getSpanEnd(urlSpan)
            var url = urlSpan.url

            // Check for the specific link pattern
            if (url.startsWith("javascript:void PM.BT.ubw(")) {
                // Extract URL parts using regex
                val regex = Regex("""'([^']+)'|[0-9]+""")
                val urlParts = regex.findAll(url).map { it.groupValues[0] }.toList()
                // Build the URL with separators
                url = urlParts.joinToString("") {
                    when (it) {
                        "58" -> ":"
                        "47" -> "/"
                        "46" -> "."
                        else -> it.removeSurrounding("'") // Remove quotes from other parts
                    }
                }
            }

            addStyle(
                style = SpanStyle(
                    color = Blue,
                ),
                start = start,
                end = end
            )
            addStringAnnotation(
                tag = "URL",
                annotation = urlSpan.url,
                start = start,
                end = end
            )
            addLink(
                url = LinkAnnotation.Url(
                    url = url,
                    linkInteractionListener = {
                        if (url.startsWith("/s/")) {
                            val searchTerm = url.substring(3) // Extract search term after "/s/"
                            // Navigate back to product list and perform search
                            onNewSearch(searchTerm)
                        } else {
                            uriHandler.openUri(url)
                        }
                    }
                ),
                start = start,
                end = end
            )
        }
    }

    Text(
        text = annotatedString,
        style = MaterialTheme.typography.bodySmall,
    )
}
