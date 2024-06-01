package rk.ui.songs

import android.content.ContentUris
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import rk.core.ALBUM_ID
import rk.core.SONGS_URI
import rk.core.SONG_DURATION
import rk.core.buildTrackMediaItem
import rk.musical.domain.model.Track

fun Track.toMediaItem() =
    buildTrackMediaItem(
        songId = id.toString(),
        title = title,
        artist = artist,
        songUri = songUri.toUri(),
        albumName = albumName,
        coverUri = coverUri?.toUri(),
        duration = duration,
        albumId = albumId
    )

fun MediaItem.toTrack() =
    Track(
        id = mediaId.toLong(),
        title = mediaMetadata.title.toString(),
        artist = mediaMetadata.artist.toString(),
        songUri = ContentUris.withAppendedId(SONGS_URI, mediaId.toLongOrNull() ?: 0L).toString(),
        albumName = mediaMetadata.albumTitle.toString(),
        coverUri = mediaMetadata.artworkUri.toString(),
        duration = mediaMetadata.extras?.getLong(SONG_DURATION, 0L) ?: 0L,
        albumId = mediaMetadata.extras?.getString(ALBUM_ID, "") ?: ""
    )

fun List<MediaItem>.toTracks() = map { it.toTrack() }

fun List<Track>.toMediaItems() = map { it.toMediaItem() }