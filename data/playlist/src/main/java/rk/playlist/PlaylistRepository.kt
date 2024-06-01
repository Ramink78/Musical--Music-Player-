package rk.playlist

import rk.musical.data.model.PlaylistModel
import rk.musical.data.model.TrackModel

typealias TrackId = Long

interface PlaylistRepository {
    suspend fun addNewPlaylist(playlist: PlaylistModel): Int
    suspend fun getAllPlaylist(): List<PlaylistModel>
    suspend fun getAllPlaylistTracks(playlistId: Long): List<TrackModel>
    suspend fun addTracksToPlaylist(tracks: List<TrackModel>, playlistId: Long)
    suspend fun getPlaylistMembersIds(playlistId: Long): List<TrackId>
}