package rk.data

import rk.core.SortOrder
import rk.data.model.AlbumMedia
import rk.musical.data.model.TrackModel

interface AlbumRepository {
    suspend fun loadAlbums(sortOrder: SortOrder): List<AlbumMedia>
    suspend fun loadAlbumMembers(albumId: String): List<TrackModel>
}