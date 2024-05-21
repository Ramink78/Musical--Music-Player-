package rk.musical.data

import rk.musical.data.model.Song

interface SongRepository {

    suspend fun loadSongs(): List<Song>
    fun getAlbumSongs(albumId: String): List<Song>
}
