package com.manutd.ronaldo.designsystem.component

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp

enum class TagType { IMDB, QUALITY, SOLID, OUTLINE,GENRE }

@Composable
 fun RoTag(
    text: String,
    type: TagType,
    modifier: Modifier = Modifier
) {
    val imdbYellow = Color(0xFFF5C518)
    val shape = RoundedCornerShape(4.dp)

    // 1. Xác định Style cho Container (Background & Border)
    val (backgroundColor, borderColor) = when (type) {
        TagType.SOLID -> Color.White to null            // Nền trắng, không viền
        TagType.OUTLINE -> Color.Transparent to Color.White // Nền trong, viền trắng
        TagType.IMDB -> Color.Transparent to imdbYellow     // Nền trong, viền vàng
        TagType.QUALITY -> Color(0xFF00C853) to null    // Nền xanh, không viền
        TagType.GENRE -> MaterialTheme.colorScheme.primaryContainer to Color.Transparent // Nền trong, viền trắng
    }

    // 2. Xây dựng nội dung Text đa màu sắc (Rich Text)
    val styledText = buildAnnotatedString {
        when (type) {
            TagType.IMDB -> {
                // Phần 1: "IMDB " màu Vàng
                withStyle(style = SpanStyle(color = imdbYellow, fontWeight = FontWeight.Bold)) {
                    append("IMDB ")
                }
                // Phần 2: Điểm số (text) màu Trắng
                withStyle(style = SpanStyle(color = Color.White)) {
                    append(text)
                }
            }

            else -> {
                // Các loại khác: 1 màu duy nhất
                val singleColor = if (type == TagType.SOLID) Color.Black else Color.White
                withStyle(style = SpanStyle(color = singleColor)) {
                    append(text)
                }
            }
        }
    }

    // 3. Render UI
    Box(
        modifier = modifier
            .then(
                if (borderColor != null) Modifier.border(1.dp, borderColor, shape)
                else Modifier
            )
            .background(backgroundColor, shape)
            .padding(horizontal = 8.dp, vertical = 4.dp)
    ) {
        Text(
            text = styledText, // Sử dụng AnnotatedString
            style = MaterialTheme.typography.labelSmall,
            fontWeight = FontWeight.Medium
        )
    }
}
