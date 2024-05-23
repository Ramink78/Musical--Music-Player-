package rk.data

import android.content.ContentUris
import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import rk.core.ALBUMS_URI
import rk.core.SortOrder
import rk.core.albumArtColumnIndex
import rk.core.albumColumns
import rk.core.albumIdColumnIndex
import rk.core.albumNameColumnIndex
import rk.core.albumSongsCountColumnIndex
import rk.core.artistColumnIndex
import rk.core.coUery
import rk.data.model.AlbumMedia


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
                            coverUri = buildCoverUri(id),
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

    private fun buildCoverUri(id: Long): String {
        val sArtworkUri = Uri
            .parse("content://media/external/audio/albumart")
        return ContentUris.withAppendedId(sArtworkUri, id).toString()
    }
}