package rk.playlist

import android.content.ContentResolver
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import rk.core.PLAYLISTS_URI
import rk.core.coUery
import rk.core.playlistColumns
import rk.core.playlistDateAddedColumnIndex
import rk.core.playlistIdColumnIndex
import rk.core.playlistTitleColumnIndex
import rk.data.SongRepository
import rk.musical.data.model.PlaylistModel
import rk.musical.data.model.TrackModel

class PlaylistRepositoryImpl(
    private val contentResolver: ContentResolver,
    private val coroutineDispatcher: CoroutineDispatcher = Dispatchers.IO,
    private val songRepository: SongRepository
) : PlaylistRepository {
    override suspend fun addNewPlaylist(playlist: PlaylistModel): Int {
        return 0
    }

    override suspend fun getAllPlaylist(): List<PlaylistModel> {
        val tempList = mutableListOf<PlaylistModel>()
        contentResolver.coUery(
            uri = PLAYLISTS_URI,
            columns = playlistColumns,
            coroutineDispatcher = coroutineDispatcher,
            onResult = {
                it?.let {
                    it.use { cursor ->
                        val idCol = cursor.playlistIdColumnIndex
                        val titleCol = cursor.playlistTitleColumnIndex
                        val dateAddedCol = cursor.playlistDateAddedColumnIndex
                        while (cursor.moveToNext()) {
                            tempList.add(
                                PlaylistModel(
                                    id = cursor.getInt(idCol),
                                    title = cursor.getString(titleCol),
                                    createdAt = cursor.getString(dateAddedCol)
                                )
                            )
                        }
                    }

                }
            }
        )
        return tempList
    }

    override suspend fun getAllPlaylistTracks(playlistId: Long): List<TrackModel> {
        val playlistTracks = mutableListOf<TrackModel>()
        val playlistCursor = contentResolver.coUery(
            uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
            columns = arrayOf(MediaStore.Audio.Playlists.Members.AUDIO_ID),
            coroutineDispatcher = coroutineDispatcher,
        )
        playlistCursor?.use { cursor ->
            while (cursor.moveToNext()) {
                val songId =
                    cursor.getLong(
                        cursor.getColumnIndexOrThrow(
                            MediaStore.Audio.Playlists.Members.AUDIO_ID
                        )
                    )
                songRepository.getSongById(songId)
                    ?.let { playlistTracks.add(it) }

            }
        }
        return playlistTracks
    }

    override suspend fun addTracksToPlaylist(tracks: List<TrackModel>, playlistId: Long) {

    }

    override suspend fun getPlaylistMembersIds(playlistId: Long): List<TrackId> {
        val membersIds = mutableListOf<TrackId>()
        contentResolver.coUery(
            uri = MediaStore.Audio.Playlists.Members.getContentUri("external", playlistId),
            columns = arrayOf(MediaStore.Audio.Playlists.Members.AUDIO_ID),
            coroutineDispatcher = coroutineDispatcher,
            onResult = { playlistCursor ->
                playlistCursor?.use { cursor ->
                    while (cursor.moveToNext()) {
                        membersIds.add(
                            cursor.getLong(
                                cursor.getColumnIndexOrThrow(
                                    MediaStore.Audio.Playlists.Members.AUDIO_ID
                                )
                            )
                        )
                    }
                }
            }
        )
        return membersIds
    }
}