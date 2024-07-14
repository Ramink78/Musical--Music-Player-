package rk.model

import rk.musical.data.model.TrackModel


data class PlaylistTrack(
    val id: Long,
    val title: String,
    val artist: String,
    val coverUri: String?,
    val trackUri: String
)

internal fun TrackModel.toPlaylistTrack() =
    PlaylistTrack(
        id = id,
        artist = artist,
        title = title,
        coverUri = coverUri,
        trackUri = songUri
    )
