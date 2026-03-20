package com.manutd.ronaldo.impl.utils

sealed class UiMode {
    // Không có panel nào mở
    object None : UiMode()

    // Phụ đề & Audio
    object Subtitles : UiMode()

    // Danh sách tập
    object Episodes : UiMode()

    // Chọn server/nguồn
    object Servers : UiMode()

    // Tốc độ phát
    object PlaybackSpeed : UiMode()

    // Resize mode
    object Resize : UiMode()

    val isNone get() = this is None
    val isSubs get() = this is Subtitles
    val isEpisodes get() = this is Episodes
    val isServers get() = this is Servers
    val isPlaybackSpeed get() = this is PlaybackSpeed
    val isResize get() = this is Resize
}