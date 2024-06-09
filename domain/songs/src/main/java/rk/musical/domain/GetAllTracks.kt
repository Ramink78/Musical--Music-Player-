package rk.musical.domain

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onEach
import rk.core.SortOrder
import rk.data.SongRepository
import rk.musical.domain.model.Track
import rk.musical.domain.model.toTrack
import javax.inject.Inject

class GetAllTracks @Inject constructor(
    private val songRepository: SongRepository
) {
    suspend operator fun invoke(sortOrder: SortOrder): List<Track> {
        return songRepository.loadSongs(sortOrder).map { it.toTrack() }
    }
    suspend fun getSongsFlow(sortOrder: SortOrder): Flow<List<Track>> {
        return songRepository.getSongsFlow(sortOrder).map { trackModels ->
            trackModels.map { it.toTrack() }
        }
    }
}