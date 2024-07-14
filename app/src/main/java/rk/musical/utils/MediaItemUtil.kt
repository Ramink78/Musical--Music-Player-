package rk.musical.utils

import android.net.Uri
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata

fun buildAlbumMediaItem(
    title: String,
    id: String,
    artist: String,
    songsCount: Int,
    coverUri: Uri?
) = MediaItem.Builder()
    .setMediaId(id)
    .setMediaMetadata(
        MediaMetadata.Builder()
            .setIsBrowsable(true)
            .setIsPlayable(false)
            .setTitle(title)
            .setArtist(artist)
            .setMediaType(MediaMetadata.MEDIA_TYPE_ALBUM)
            .setTotalTrackCount(songsCount)
            .setArtworkUri(coverUri)
            .build()
    )
    .build()
