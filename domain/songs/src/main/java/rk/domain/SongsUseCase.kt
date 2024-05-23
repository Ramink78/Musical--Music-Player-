package rk.domain

import rk.core.SortOrder
import rk.data.SongRepository
import rk.domain.model.Track
import rk.domain.model.toTrack
import javax.inject.Inject

class SongsUseCase @Inject constructor(
    private val songRepository: SongRepository
) {
    suspend fun loadSongs(order: SortOrder): List<Track> {
        return songRepository.loadSongs(order = order).map { it.toTrack() }
    }

    suspend fun getAlbumSongs(albumId: String): List<Track> {
        return songRepository.getAlbumSongs(albumId).map { it.toTrack() }
    }
}