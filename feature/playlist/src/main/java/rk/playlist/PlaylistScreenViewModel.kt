package rk.playlist

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rk.model.PlaylistTrack
import javax.inject.Inject

@HiltViewModel
class PlaylistScreenViewModel @Inject constructor(
    private val playlistsUseCase: GetAllPlaylistsUseCase,
    private val playlistTracksUseCase: GetPlaylistTracksUseCase
) : ViewModel() {
    val uiState = MutableStateFlow(PlaylistScreenState())

    fun loadPlaylists() {
        uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val playlists = playlistsUseCase.getAllPlaylists().map {
                val playlistTracks = getPlaylistTracks(it.id.toLong())
                it.copy(
                    coverUri =
                    if (playlistTracks.isEmpty())
                        null
                    else
                        playlistTracks.first().coverUri
                )
            }
            uiState.update {
                it.copy(
                    isLoading = false, isEmpty = playlists.isEmpty(), playlists = playlists
                )
            }

        }

    }

    private suspend fun getPlaylistTracks(playlistId: Long): List<PlaylistTrack> {
        uiState.update { it.copy(isLoading = true) }
        return playlistTracksUseCase(playlistId)

    }
}