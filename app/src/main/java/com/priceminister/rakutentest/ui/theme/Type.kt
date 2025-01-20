package com.priceminister.rakutentest.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.priceminister.rakutentest.R

val RakutenFontFamily = FontFamily(
    Font(R.font.rakutensansui_w_bd, FontWeight.Bold),
    Font(R.font.rakutensansui_w_bdit, FontWeight.Bold, FontStyle.Italic),
    Font(R.font.rakutensansui_w_blk, FontWeight.Black),
    Font(R.font.rakutensansui_w_blkit, FontWeight.Black, FontStyle.Italic),
    Font(R.font.rakutensansui_w_it, FontWeight.Normal, FontStyle.Italic),
    Font(R.font.rakutensansui_w_lt, FontWeight.Light),
    Font(R.font.rakutensansui_w_ltit, FontWeight.Light, FontStyle.Italic),
    Font(R.font.rakutensansui_w_rg, FontWeight.Normal),
    Font(R.font.rakutensansui_w_sbd, FontWeight.SemiBold),
    Font(R.font.rakutensansui_w_sbdit, FontWeight.SemiBold, FontStyle.Italic)
)

// Set of Material typography styles to start with
val Typography = Typography(
    bodyLarge = TextStyle(
        fontFamily = RakutenFontFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    )
    /* Other default text styles to override
    titleLarge = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 22.sp,
        lineHeight = 28.sp,
        letterSpacing = 0.sp
    ),
    labelSmall = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Medium,
        fontSize = 11.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
    */
)