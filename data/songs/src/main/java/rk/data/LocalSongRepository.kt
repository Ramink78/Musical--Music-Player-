package rk.data

import android.content.ContentUris
import android.content.Context
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import rk.core.ALBUM_ID
import rk.core.IS_MUSIC_CLAUSE
import rk.core.SONGS_URI
import rk.core.SortOrder
import rk.core.albumIdColumnIndex
import rk.core.albumNameColumnIndex
import rk.core.artistColumnIndex
import rk.core.coUery
import rk.core.songColumns
import rk.core.songDurationColumnIndex
import rk.core.songIdColumnIndex
import rk.core.songNameColumnIndex
import rk.core.toSortClause
import rk.musical.data.model.TrackModel

internal class LocalSongRepository(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : SongRepository {
    private var cachedTracks = listOf<TrackModel>()
    override suspend fun loadSongs(order: SortOrder): List<TrackModel> {
        val tempList = mutableListOf<TrackModel>()
        context.contentResolver.coUery(
            uri = SONGS_URI,
            columns = songColumns,
            sortOrder = order.toSortClause(),
            selection = IS_MUSIC_CLAUSE,
            coroutineDispatcher = dispatcher,
            onResult = {
                it?.use { cursor ->
                    val idCol = cursor.songIdColumnIndex
                    val titleCol = cursor.songNameColumnIndex
                    val artistCol = cursor.artistColumnIndex
                    val albumNameCol = cursor.albumNameColumnIndex
                    val songDurationCol = cursor.songDurationColumnIndex
                    val albumIdCol = cursor.albumIdColumnIndex
                    while (cursor.moveToNext()) {
                        val songId = cursor.getLong(idCol)
                        val songUri = ContentUris.withAppendedId(SONGS_URI, songId)
                        val albumId = cursor.getLong(albumIdCol).toString()
                        tempList.add(
                            TrackModel(
                                id = songId,
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
                    cachedTracks = tempList
                }
            }
        )

        return tempList


    }

    override suspend fun getSongById(songId: Long): TrackModel? {
        if (cachedTracks.isEmpty())
            loadSongs(SortOrder.DateAddedDesc)
        return cachedTracks.find { it.id == songId }
    }

    override suspend fun getAlbumSongs(albumId: String): List<TrackModel> {
        val tempList = mutableListOf<TrackModel>()
        context.contentResolver.coUery(
            uri = SONGS_URI,
            columns = songColumns,
            sortOrder = SortOrder.NameAsc.toSortClause(),
            selection = "$ALBUM_ID=$albumId",
            coroutineDispatcher = dispatcher,
            onResult = {
                it?.use { cursor ->
                    val idCol = cursor.songIdColumnIndex
                    val titleCol = cursor.songNameColumnIndex
                    val artistCol = cursor.artistColumnIndex
                    val albumNameCol = cursor.albumNameColumnIndex
                    val songDurationCol = cursor.songDurationColumnIndex
                    while (cursor.moveToNext()) {
                        val songId = cursor.getLong(idCol)
                        val songUri = ContentUris.withAppendedId(SONGS_URI, songId)
                        tempList.add(
                            TrackModel(
                                id = songId,
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
                }
            }
        )
        return tempList
    }

    private fun buildCoverUri(id: Long): String {
        return "content://media/external/audio/media/$id/albumart"
    }
}