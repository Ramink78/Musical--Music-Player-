package rk.musical.playlist.detail

import rk.model.PlaylistTrack

data class PlaylistDetailScreenUiModel(
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
    val tracks: List<PlaylistTrack> = emptyList()
)
