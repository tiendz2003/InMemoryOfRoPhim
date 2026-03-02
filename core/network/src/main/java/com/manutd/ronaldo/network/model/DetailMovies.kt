package com.manutd.ronaldo.network.model

enum class MovieType { SERIES, MOVIE }

enum class RatingSource { IMDB, TMDB }
enum class CastRole { MAIN, SUPPORTING, GUEST }
sealed class AiringStatus {
    data class OnAir(val currentEpisode: Int, val totalEpisode: Int) : AiringStatus()
    object Completed : AiringStatus()
    data class Upcoming(val releaseDate: String) : AiringStatus()
    object NotApplicable : AiringStatus()  // Phim lẻ
}

data class MovieRating(
    val source: RatingSource,
    val score: Float,
    val maxScore: Float = 10f
)

data class AudioTrack(
    val id: String,
    val label: String,   // "Vietsub #1", "Thuyết minh"
    val language: String // "vi", "en", "ko"
)

data class Episode(
    val id: String,
    val episodeNumber: Int,
    val title: String?,
    val thumbnailUrl: String?,
    val duration: Int,          // seconds
    val videoUrl: String,
    val isWatched: Boolean = false,
    val watchProgress: Float = 0f
)

data class Season(
    val seasonNumber: Int,
    val title: String,
    val episodes: List<Episode>,
    val audioTracks: List<AudioTrack>,
    val defaultAudioTrackId: String
)

data class CastMember(
    val id: String,
    val name: String,
    val characterName: String,
    val profileImageUrl: String?,
    val role: CastRole
)

data class Genre(val id: String, val name: String)

data class MovieRecommendation(
    val id: String,
    val title: String,
    val posterUrl: String,
    val rating: Float,
    val type: MovieType,
    val genres: List<Genre>
)

data class MovieDetail(
    val id: String,
    val title: String,
    val originalTitle: String,
    val posterUrl: String,
    val trailerUrl: String,
    val ratings: List<MovieRating>,
    val classification: String,   // "T13", "T16", "P"
    val releaseYear: Int,
    val type: MovieType,

    // Series-only
    val currentSeason: Int? = null,
    val totalSeasons: Int? = null,
    val latestEpisode: Int? = null,
    val airingStatus: AiringStatus = AiringStatus.NotApplicable,

    val genres: List<Genre>,
    val synopsis: String,
    val seasons: List<Season> = emptyList(),

    // User state
    val isFavorited: Boolean = false,
    val isInWatchlist: Boolean = false,
    val userRating: Float? = null
)