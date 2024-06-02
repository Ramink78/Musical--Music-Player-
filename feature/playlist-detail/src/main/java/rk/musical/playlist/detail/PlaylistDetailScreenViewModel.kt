package rk.musical.playlist.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rk.playlist.GetPlaylistTracksUseCase
import javax.inject.Inject

@HiltViewModel
class PlaylistDetailScreenViewModel @Inject constructor(
    private val getPlaylistTracksUseCase: GetPlaylistTracksUseCase
) : ViewModel() {
    private val _uiState = MutableStateFlow(PlaylistDetailScreenUiModel())
    val uiState = _uiState.asStateFlow()

    fun loadPlaylistTracks(playlistId: Long) {
        _uiState.update { it.copy(isLoading = false) }
        viewModelScope.launch {
            val playlistTracks = getPlaylistTracksUseCase(playlistId)
            _uiState.update {
                it.copy(
                    isLoading = false,
                    isEmpty = playlistTracks.isEmpty(),
                    tracks = playlistTracks
                )
            }
        }
    }
}