package rk.musical.data

import android.content.ContentUris
import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.withContext
import rk.musical.data.model.Song
import rk.musical.utils.IS_MUSIC_CLAUSE
import rk.musical.utils.SONGS_URI
import rk.musical.utils.SortOrder
import rk.musical.utils.albumIdColumnIndex
import rk.musical.utils.albumNameColumnIndex
import rk.musical.utils.artistColumnIndex
import rk.musical.utils.kuery
import rk.musical.utils.songColumns
import rk.musical.utils.songDurationColumnIndex
import rk.musical.utils.songIdColumnIndex
import rk.musical.utils.songNameColumnIndex

class LocalSongRepository(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SongRepository {
    var chacedSongs: List<Song> = emptyList()
        private set
    private val _localSongs = MutableSharedFlow<List<Song>>()

    private suspend fun loadSongs(sortOrder: String = SortOrder.Descending.dateAdded): List<Song> {
        return withContext(dispatcher) {
            context.contentResolver.kuery(
                uri = SONGS_URI,
                columns = songColumns,
                sortOrder = sortOrder,
                selection = IS_MUSIC_CLAUSE
            )?.use { cursor ->
                val idCol = cursor.songIdColumnIndex
                val titleCol = cursor.songNameColumnIndex
                val artistCol = cursor.artistColumnIndex
                val albumNameCol = cursor.albumNameColumnIndex
                val songDurationCol = cursor.songDurationColumnIndex
                val albumIdCol = cursor.albumIdColumnIndex
                val tempList = mutableListOf<Song>()
                while (cursor.moveToNext()) {
                    val songId = cursor.getLong(idCol)
                    val songUri = ContentUris.withAppendedId(SONGS_URI, songId)
                    val albumId = cursor.getLong(albumIdCol).toString()
                    tempList.add(
                        Song(
                            id = songId.toString(),
                            title = cursor.getString(titleCol),
                            artist = cursor.getString(artistCol),
                            songUri = songUri.toString(),
                            albumName = cursor.getString(albumNameCol),
                            coverUri = buildCoverUri(songId),
                            duration = cursor.getLong(songDurationCol),
                            albumId = albumId
                        )
                    )
                }
                chacedSongs = tempList
                _localSongs.emit(tempList)
            }
            chacedSongs
        }
    }

    private fun buildCoverUri(id: Long): String {
        return "content://media/external/audio/media/$id/albumart"
    }

    override fun getAlbumSongs(albumId: String): List<Song> {
        return chacedSongs.filter { it.albumId == albumId }
    }

    override suspend fun loadSongs(): List<Song> {
        return loadSongs(sortOrder = SortOrder.Descending.dateAdded)
    }
}
