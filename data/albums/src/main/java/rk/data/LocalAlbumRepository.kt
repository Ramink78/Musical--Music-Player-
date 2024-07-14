package rk.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import rk.core.ALBUMS_URI
import rk.core.ALBUM_ID
import rk.core.SONGS_URI
import rk.core.SortOrder
import rk.core.albumColumns
import rk.core.albumIdColumnIndex
import rk.core.albumNameColumnIndex
import rk.core.albumSongsCountColumnIndex
import rk.core.artistColumnIndex
import rk.core.coUery
import rk.core.songColumns
import rk.core.songDurationColumnIndex
import rk.core.songIdColumnIndex
import rk.core.songNameColumnIndex
import rk.data.model.AlbumMedia
import rk.musical.data.model.TrackModel


internal class LocalAlbumRepository(
    private val context: Context,
    private val dispatcher: CoroutineDispatcher = Dispatchers.IO
) : AlbumRepository {
    override suspend fun loadAlbums(sortOrder: SortOrder): List<AlbumMedia> {
        val tempList = mutableListOf<AlbumMedia>()

        context.contentResolver.coUery(
            uri = ALBUMS_URI,
            columns = albumColumns,
            coroutineDispatcher = dispatcher,
            sortOrder = MediaStore.Audio.Albums.DEFAULT_SORT_ORDER
        ) { cursor ->
            cursor?.use {
                val idCol = it.albumIdColumnIndex
                val artistCol = it.artistColumnIndex
                val albumNameCol = it.albumNameColumnIndex
                val albumSongsCountCol = it.albumSongsCountColumnIndex
                while (cursor.moveToNext()) {
                    val id = it.getLong(idCol)
                    tempList.add(
                        AlbumMedia(
                            id = id.toString(),
                            coverUri = buildAlbumCoverUri(id),
                            artist = it.getString(artistCol),
                            songsCount = it.getInt(albumSongsCountCol),
                            title = it.getString(albumNameCol)
                        )
                    )
                }

            }
        }
        return tempList
    }

    override suspend fun loadAlbumMembers(albumId: String): List<TrackModel> {
        val members = mutableListOf<TrackModel>()
        context.contentResolver.coUery(
            uri = SONGS_URI,
            columns = songColumns,
            selection = "$ALBUM_ID=?",
            selectionArgs = arrayOf(albumId),
            coroutineDispatcher = dispatcher
        ) { cursor ->
            cursor?.use {
                val idCol = cursor.songIdColumnIndex
                val titleCol = cursor.songNameColumnIndex
                val artistCol = cursor.artistColumnIndex
                val albumNameCol = cursor.albumNameColumnIndex
                val songDurationCol = cursor.songDurationColumnIndex
                while (it.moveToNext()) {
                    val songId = cursor.getLong(idCol)
                    val songUri = ContentUris.withAppendedId(SONGS_URI, songId)
                    members.add(
                        TrackModel(
                            id = songId,
                            title = cursor.getString(titleCol),
                            artist = cursor.getString(artistCol),
                            songUri = songUri.toString(),
                            albumName = cursor.getString(albumNameCol),
                            coverUri = buildTrackCoverUri(songId),
                            duration = cursor.getLong(songDurationCol),
                            albumId = albumId
                        )
                    )

                }
            }

        }
        return members
    }

    private fun buildAlbumCoverUri(id: Long): String {
        val sArtworkUri = Uri
            .parse("content://media/external/audio/albumart")
        return ContentUris.withAppendedId(sArtworkUri, id).toString()
    }

    private fun buildTrackCoverUri(id: Long): String {
        return "content://media/external/audio/media/$id/albumart"
    }
}