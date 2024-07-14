package rk.playlist

import rk.model.Playlist

data class PlaylistScreenState(
    val isLoading: Boolean = true,
    val isEmpty: Boolean = true,
    val playlists: List<Playlist> = emptyList()
)
