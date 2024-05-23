package rk.data

import rk.data.model.SongMedia

interface SongRepository {
    suspend fun loadSongs(): List<SongMedia>
}