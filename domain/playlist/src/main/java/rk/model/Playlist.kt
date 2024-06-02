package rk.model

import rk.musical.data.model.PlaylistModel


data class Playlist(
    val id: Int,
    val title: String,
    val createdAt: String,
    val coverUri: String?
)

fun PlaylistModel.toPlaylist() =
    Playlist(
        id = id,
        title = title,
        createdAt = createdAt,
        coverUri = null
    )
