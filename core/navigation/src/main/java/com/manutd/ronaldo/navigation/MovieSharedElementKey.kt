package com.manutd.ronaldo.navigation

data class MovieSharedElementKey(
    val movieId: String,
    val origin: String,
    val type: MovieSharedElementType,
)

enum class MovieSharedElementType {
    /** Toàn bộ card container → full screen container */
    Bounds,

    /** Ảnh poster */
    Poster,

    /** Tiêu đề phim */
    Title,
}
