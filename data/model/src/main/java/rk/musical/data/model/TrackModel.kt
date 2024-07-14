package rk.musical.data.model

data class TrackModel(
    val id: Long,
    val title: String,
    val artist: String,
    val songUri: String,
    val albumName: String,
    val duration: Long,
    val coverUri: String? = null,
    val albumId: String
)
