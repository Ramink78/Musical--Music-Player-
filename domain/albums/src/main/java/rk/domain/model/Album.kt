package rk.domain.model

import rk.data.model.AlbumMedia

data class Album(
    val id: String,
    val title: String,
    val artist: String,
    val songsCount: Int,
    val coverUri: String? = null
)

fun AlbumMedia.toAlbum() =
    Album(
        id = id,
        title = title,
        artist = artist,
        songsCount = songsCount,
        coverUri = coverUri
    )
