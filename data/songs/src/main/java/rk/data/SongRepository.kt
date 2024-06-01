package rk.data

import rk.core.SortOrder
import rk.musical.data.model.TrackModel

interface SongRepository {
    suspend fun loadSongs(order: SortOrder = SortOrder.DateAddedDesc): List<TrackModel>
    suspend fun getSongById(songId: Long): TrackModel?
    suspend fun getAlbumSongs(albumId: String): List<TrackModel>
}