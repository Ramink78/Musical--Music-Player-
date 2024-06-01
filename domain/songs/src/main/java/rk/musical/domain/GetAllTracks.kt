package rk.musical.domain

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
}