package rk.domain

import rk.data.SongRepository
import rk.domain.model.Track
import rk.domain.model.toTrack
import javax.inject.Inject

class SongsUseCase @Inject constructor(
    private val songRepository: SongRepository
) {
    suspend fun getSongs(): List<Track> {
        return songRepository.loadSongs().map { it.toTrack() }
    }
}