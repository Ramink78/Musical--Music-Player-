package rk.playlist

import rk.model.toPlaylist
import javax.inject.Inject

class GetAllPlaylistsUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository
) {
    suspend fun getAllPlaylists() =
        playlistRepository.getAllPlaylist().map { it.toPlaylist() }

}