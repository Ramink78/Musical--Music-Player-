package rk.musical.data

import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import rk.core.SortOrder
import rk.domain.AlbumsUseCase
import rk.domain.SongsUseCase
import rk.musical.data.model.convertToMediaItems
import rk.musical.data.model.toMediaItem
import rk.musical.data.model.toMediaItems
import rk.playbackservice.MediaItemTree

class MediaItemTreeImpl(
    private val albumsUseCase: AlbumsUseCase,
    private val songsUseCase: SongsUseCase,
    private val favoriteRepository: FavoriteRepository
) : MediaItemTree {
    private val idToChildren = mutableMapOf<String, MutableList<MediaItem>>()
    private val idToMediaItem = mutableMapOf<String, MediaItem>()
    private val rootId = "/"
    private val albumCategoryId = "Album"
    private val recentCategoryId = "Recent"
    private val favoriteCategoryId = "Favorite"
    private val rootMediaItem = MediaItem.Builder().setMediaId(rootId).setMediaMetadata(
        MediaMetadata.Builder().setIsPlayable(false).setIsBrowsable(true).build()
    ).build()
    private val albumCategory = MediaItem.Builder().setMediaId(albumCategoryId).setMediaMetadata(
        MediaMetadata.Builder().setTitle(albumCategoryId).setIsPlayable(false).setIsBrowsable(true)
            .build()
    ).build()
    private val recentCategory = MediaItem.Builder().setMediaId(recentCategoryId).setMediaMetadata(
        MediaMetadata.Builder().setTitle(recentCategoryId).setIsPlayable(false).setIsBrowsable(true)
            .build()
    ).build()
    private val favoriteCategory =
        MediaItem.Builder().setMediaId(favoriteCategoryId).setMediaMetadata(
            MediaMetadata.Builder().setTitle(favoriteCategoryId).setIsPlayable(false)
                .setIsBrowsable(true).build()
        ).build()

    init {
        initRoot()
    }

    private fun initRoot() {
        val rootList = mutableListOf<MediaItem>()
        rootList += albumCategory
        rootList += favoriteCategory
        rootList += recentCategory
        idToChildren[rootId] = rootList
    }

    private suspend fun initAlbumCategory() {
        val albumList = mutableListOf<MediaItem>()
        val loadedAlbums = albumsUseCase.loadAlbums(SortOrder.DateAddedDesc)
        albumList.addAll(loadedAlbums.toMediaItems())
        loadedAlbums.forEach {
            idToMediaItem[it.id] = it.toMediaItem()
        }
        songsUseCase.loadSongs(SortOrder.DateAddedDesc).forEach {
            idToMediaItem[it.id] = it.toMediaItem()
        }
        idToChildren[albumCategoryId] = albumList
        val songsByAlbumId =
            songsUseCase.loadSongs(SortOrder.DateAddedDesc).groupBy { it.albumId }.mapValues {
                it.value.convertToMediaItems().toMutableList()
            }
        idToChildren.putAll(songsByAlbumId)
    }

    private suspend fun initFavoriteCategory() {
        val favoriteList = mutableListOf<MediaItem>()
        favoriteList.addAll(favoriteRepository.getAllFavorites().toMediaItems())
        idToChildren[favoriteCategoryId] = favoriteList
    }

    override fun getRootMediaItem(): MediaItem {
        return rootMediaItem
    }

    override suspend fun getChildren(parentId: String): List<MediaItem> {
        initAlbumCategory()
        initFavoriteCategory()
        return idToChildren[parentId]?.toList() ?: emptyList()
    }

    override suspend fun getMediaItem(mediaId: String): MediaItem? {
        return idToMediaItem[mediaId]
    }
}
