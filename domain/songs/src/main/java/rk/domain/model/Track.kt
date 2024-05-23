package rk.domain.model

import rk.data.model.SongMedia

data class Track(
    val id: String,
    val title: String,
    val artist: String,
    val songUri: String,
    val albumName: String,
    val duration: Long,
    val coverUri: String? = null,
    val albumId: String
)

fun SongMedia.toTrack() =
    Track(
        id = id,
        title = title,
        artist = artist,
        songUri = songUri,
        albumName = albumName,
        duration = duration,
        coverUri = coverUri,
        albumId = albumId,
    )