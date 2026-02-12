package com.manutd.ronaldo.designsystem.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil3.compose.AsyncImage
import coil3.request.ImageRequest
import coil3.request.crossfade

@Composable
fun RoThumbnailImage(
    model: String,
    scale: ContentScale = ContentScale.Crop,
    contentDescription: String?,
    modifier: Modifier = Modifier
) {
    AsyncImage(
        model = ImageRequest.Builder(LocalContext.current)
            .data(model)
            .size(width = 300, height = 450)
            .crossfade(true)
            .build(),
        contentScale = scale,
        contentDescription = contentDescription,
        modifier = modifier

    )
}