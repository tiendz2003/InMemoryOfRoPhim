package com.manutd.ronaldo.network.model

import android.R.attr.rating
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import javax.annotation.concurrent.Immutable

@Immutable
data class Actor(
    val id: String,
    val name: String,
    val photoUrl: String,
    val gender: Gender,
)

enum class Gender(val displayName: String) {
    MALE("Nam"),
    FEMALE("Nữ"),
    OTHER("Khác")
}

@Immutable
data class HomeData(
    val color: String,
    val description: String,
    val gridNumber: Int,
    val groups: ImmutableList<Group>,
    val id: String,
    val image: String,
    val name: String,
)

@Immutable
data class Group(
    val id: String,
    val display: String,
    val name: String?,
    val channels: ImmutableList<Channel>,
    val remoteUrl: String?,
    val type: ChannelType = ChannelType.UNKNOWN,
    val actors: ImmutableList<Actor> = persistentListOf()
)

@Immutable
data class Channel(
    val id: String,
    val name: String,
    val display: String,
    val description: String,
    val logoUrl: String,
    val streamUrl: String,
    val shareUrl: String,
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
    UNKNOWN,
    ACTOR
}

