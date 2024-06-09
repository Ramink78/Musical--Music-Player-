package rk.data

import kotlinx.coroutines.flow.Flow
import rk.core.SortOrder
import rk.musical.data.model.TrackModel

interface SongRepository {
    suspend fun getSongsFlow(sortOrder: SortOrder): Flow<List<TrackModel>>
    suspend fun loadSongs(order: SortOrder = SortOrder.DateAddedDesc): List<TrackModel>
    suspend fun getSongById(songId: Long): TrackModel?
    suspend fun getAlbumSongs(albumId: String): List<TrackModel>
}