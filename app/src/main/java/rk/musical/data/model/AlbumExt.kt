package rk.musical.data.model

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import rk.domain.model.Album
import rk.musical.utils.buildAlbumMediaItem

fun Album.toMediaItem() =
    buildAlbumMediaItem(
        title = title,
        artist = artist,
        coverUri = coverUri?.toUri(),
        songsCount = songsCount,
        id = id
    )

fun List<Album>.toMediaItems() = map { it.toMediaItem() }

fun List<MediaItem>.toAlbums() = map { it.toAlbum() }

fun MediaItem.toAlbum() =
    Album(
        id = mediaId,
        title = mediaMetadata.title.toString(),
        artist = mediaMetadata.artist.toString(),
        songsCount = mediaMetadata.totalTrackCount ?: 0,
        coverUri = mediaMetadata.artworkUri.toString()
    )
