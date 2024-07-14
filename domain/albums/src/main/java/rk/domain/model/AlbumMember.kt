package rk.domain.model

import androidx.core.net.toUri
import rk.core.buildTrackMediaItem
import rk.musical.data.model.TrackModel

data class AlbumMember(
    val id: Long,
    val title: String,
    val artist: String,
    val songUri: String,
    val coverUri: String?,
    val albumName: String,
    val duration: Long,
    val albumId: String
)

fun TrackModel.toAlbumMember() =
    AlbumMember(
        id = id,
        title = title,
        artist = artist,
        songUri = songUri,
        coverUri = coverUri,
        albumName = albumName,
        albumId = albumId,
        duration = duration
    )

fun AlbumMember.toMediaItem() =
    buildTrackMediaItem(
        songId = id.toString(),
        title = title,
        artist = artist,
        songUri = songUri.toUri(),
        albumName = albumName,
        coverUri = coverUri?.toUri(),
        duration = duration,
        albumId = albumId
    )

