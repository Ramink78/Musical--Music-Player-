package rk.musical.domain.model

import rk.musical.data.model.TrackModel

data class Track(
    val id: Long,
    val title: String,
    val artist: String,
    val coverUri: String?,
    val songUri: String,
    val albumName: String,
    val duration: Long,
    val albumId: String,
)

fun TrackModel.toTrack() =
    Track(
        id = id,
        title = title,
        artist = artist,
        coverUri = coverUri,
        songUri = songUri,
        albumName = albumName,
        duration = duration,
        albumId = albumId
    )
