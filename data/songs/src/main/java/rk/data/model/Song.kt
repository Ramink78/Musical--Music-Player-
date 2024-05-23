package rk.data.model


data class SongMedia(
    val id: String,
    val title: String,
    val artist: String,
    val songUri: String,
    val albumName: String,
    val duration: Long,
    val coverUri: String? = null,
    val albumId: String
)

