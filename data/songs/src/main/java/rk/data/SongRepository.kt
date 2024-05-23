package rk.data

import rk.core.SortOrder
import rk.data.model.SongMedia

interface SongRepository {
    suspend fun loadSongs(order: SortOrder = SortOrder.DateAddedDesc): List<SongMedia>
}