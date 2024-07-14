package rk.domain

import rk.data.AlbumRepository
import rk.domain.model.AlbumMember
import rk.domain.model.toAlbumMember
import javax.inject.Inject

class GetAlbumMembersUseCase @Inject constructor(
    private val albumRepository: AlbumRepository
) {
    suspend operator fun invoke(albumId: String): List<AlbumMember> {
        return albumRepository.loadAlbumMembers(albumId).map { it.toAlbumMember() }
    }
}