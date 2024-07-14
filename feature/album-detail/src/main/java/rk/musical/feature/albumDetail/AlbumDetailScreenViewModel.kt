package rk.musical.feature.albumDetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rk.core.player.MusicalRemote
import rk.domain.GetAlbumMembersUseCase
import rk.domain.model.AlbumMember
import rk.domain.model.toMediaItem
import javax.inject.Inject

@HiltViewModel
class AlbumDetailScreenViewModel
@Inject
constructor(
    private val getAlbumMembers: GetAlbumMembersUseCase,
    private val musicalRemote: MusicalRemote
) : ViewModel() {
    private val _uiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currentSongs = emptyList<MediaItem>()
    val playingSong =
        musicalRemote.playingMediaItemFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = null
            )

    fun getAlbumChildren(albumId: String) {
        _uiState.update { AlbumDetailUiState.Loading }
        viewModelScope.launch {
            val members = getAlbumMembers(albumId)
            _uiState.update { AlbumDetailUiState.Loaded(members) }
            currentSongs = members.map { it.toMediaItem() }
        }
    }

    fun playSong(index: Int) {
        if (musicalRemote.currentPlaylist != currentSongs) {
            musicalRemote.setPlaylist(currentSongs)
        }
        musicalRemote.playSong(index)
    }
}

sealed interface AlbumDetailUiState {
    data object Loading : AlbumDetailUiState
    data class Loaded(
        val members: List<AlbumMember>
    ) : AlbumDetailUiState
}
