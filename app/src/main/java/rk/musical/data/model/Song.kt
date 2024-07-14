package rk.musical.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favorite")
data class Song(
    @PrimaryKey val id: String,
    val title: String,
    val artist: String,
    val songUri: String,
    val albumName: String,
    val duration: Long,
    val coverUri: String? = null,
    val albumId: String
) {
    companion object {
        val Empty =
            Song(
                id = "",
                title = "",
                artist = "",
                songUri = "",
                albumName = "",
                duration = 0,
                albumId = ""
            )
    }
}
