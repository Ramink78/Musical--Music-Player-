package rk.data

import rk.core.SortOrder
import rk.data.model.AlbumMedia

interface AlbumRepository {
    suspend fun loadAlbums(sortOrder: SortOrder): List<AlbumMedia>
}