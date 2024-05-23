package rk.domain

import rk.core.SortOrder
import rk.data.AlbumRepository
import rk.domain.model.Album
import rk.domain.model.toAlbum
import javax.inject.Inject

class AlbumsUseCase @Inject constructor(
    private val albumRepository: AlbumRepository
) {
    suspend fun loadAlbums(order: SortOrder): List<Album> {
        return albumRepository.loadAlbums(order).map { it.toAlbum() }
    }
}