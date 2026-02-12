package com.manutd.ronaldo.network.model

import android.R.attr.rating
import javax.annotation.concurrent.Immutable

@Immutable
data class HomeData(
    val color: String,
    val description: String,
    val gridNumber: Int,
    val groups: List<Group>,
    val id: String,
    val image: String,
    val name: String,
)
@Immutable
data class Group(
    val id: String,
    val display: String,
    val name: String?,
    val channels: List<Channel>,
    val remoteUrl: String?,
    val type: ChannelType = ChannelType.UNKNOWN,
    // từ Group.RemoteData.url
)
@Immutable
data class Channel(
    val id: String,
    val name: String,
    val display: String,
    val description: String,
    val logoUrl: String,        // từ Channel.Image.url
    val streamUrl: String,      // từ Channel.RemoteData.url
    val shareUrl: String,       // từ Channel.Share.url
    val imdb: String,
    val quality: String,
    val rating: String,
    val year: String,
    val episode: String
)

enum class ChannelType {
    HORIZONTAL,
    SLIDER,
    TOP,
    UNKNOWN
}


fun HomeResponse.toDomain(): HomeData {
    return HomeData(
        color = color,
        description = description,
        gridNumber = gridNumber,
        groups = groups.map { it.toDomain() },
        id = id,
        image = image.url,
        name = name
    )
}

fun HomeResponse.Group.toDomain(): Group {
    val type = when (display.lowercase()) {
        "horizontal" -> {
            if (name?.contains("top", ignoreCase = true) == true) {
                ChannelType.TOP
            } else {
                ChannelType.HORIZONTAL
            }
        }

        "slider" -> ChannelType.SLIDER
        else -> ChannelType.UNKNOWN
    }
    return Group(
        id = id,
        display = display,
        name = name,
        channels = channels.map { it.toDomain() },
        remoteUrl = remoteData?.url,
        type = type
    )
}

fun HomeResponse.Group.Channel.toDomain(): Channel {
    return Channel(
        id = id,
        name = name,
        display = display,
        description = description,
        logoUrl = image.url,
        streamUrl = remoteData.url,
        shareUrl = share.url,
        imdb = "7.0",
        quality = "HD",
        rating = "3.5",
        year = "2025",
        episode = "34"
    )
}