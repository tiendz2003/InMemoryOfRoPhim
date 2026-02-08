package com.manutd.ronaldo.network.model


data class HomeData(
    val color: String,
    val description: String,
    val gridNumber: Int,
    val groups: List<Group>,
    val id: String,
    val image: String,
    val name: String,
)

data class Group(
    val id: String,
    val display: String,
    val name: String?,
    val channels: List<Channel>,
    val remoteUrl: String?,
    val type: ChannelType = ChannelType.UNKNOWN,
    // từ Group.RemoteData.url
)

data class Channel(
    val id: String,
    val name: String,
    val display: String,
    val description: String,
    val logoUrl: String,        // từ Channel.Image.url
    val streamUrl: String,      // từ Channel.RemoteData.url
    val shareUrl: String        // từ Channel.Share.url
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

data class SectionMovies(
    val sliderMovies: Group,
    val horizontalMovies: List<Group>,
    val topMovies: List<Group>
)

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
        shareUrl = share.url
    )
}