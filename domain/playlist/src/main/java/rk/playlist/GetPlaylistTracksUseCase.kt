package rk.playlist

import rk.data.SongRepository
import rk.model.PlaylistTrack
import rk.model.toPlaylistTrack
import javax.inject.Inject

class GetPlaylistTracksUseCase @Inject constructor(
    private val playlistRepository: PlaylistRepository,
    private val songsRepository: SongRepository,
) {

    suspend fun getPlaylistTracks(playlistId: Long): List<PlaylistTrack> {
        val playlistTracks = mutableListOf<PlaylistTrack>()
        val membersId = playlistRepository.getPlaylistMembersIds(playlistId)
        membersId.forEach { id ->
            songsRepository.getSongById(id)?.let { playlistTracks.add(it.toPlaylistTrack()) }
        }
        return playlistTracks

    }
}