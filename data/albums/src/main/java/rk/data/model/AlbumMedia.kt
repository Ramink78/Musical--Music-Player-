package rk.data.model

data class AlbumMedia(
    val id: String,
    val title: String,
    val artist: String,
    val songsCount: Int,
    val coverUri: String? = null
)
