package rk.ui.nowplaying.collapsed

data class MiniNowPlayingState(
    val isVisible: Boolean = false,
    val isPlaying: Boolean = false,
    val title: String = "",
    val coverUri: String? = null
)
