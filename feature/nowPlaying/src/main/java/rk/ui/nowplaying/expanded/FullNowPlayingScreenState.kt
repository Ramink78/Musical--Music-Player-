package rk.ui.nowplaying.expanded

import rk.core.PlayerState

data class FullNowPlayingScreenState(
    val playerState: PlayerState = PlayerState.idle,
    val isLyricMode: Boolean = false
)
