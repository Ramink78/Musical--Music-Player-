package rk.ui.songs

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rk.core.SortOrder
import rk.core.player.MusicalRemote
import rk.musical.domain.GetAllTracks
import javax.inject.Inject

@HiltViewModel
class SongsScreenViewModel
@Inject
constructor(
    private val getAllTracks: GetAllTracks,
    private val musicalRemote: MusicalRemote
) : ViewModel() {
    private val _uiState = MutableStateFlow(SongsScreenUiModel())
    val uiState = _uiState.asStateFlow()

    private var currentMediaItems = emptyList<MediaItem>()

    val playingSongFlow = musicalRemote.playingMediaItemFlow.onEach {
        it?.let { mediaItem ->

            _uiState.update { uiModel ->
                uiModel.copy(
                    currentTrack = mediaItem.toTrack()
                )
            }
        }
    }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000), initialValue = null
            )

    fun playSong(index: Int) {
        if (musicalRemote.currentPlaylist != currentMediaItems) {
            musicalRemote.setPlaylist(currentMediaItems)
        }
        musicalRemote.playSong(index)
    }
    fun loadSongs(order: SortOrder) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            getAllTracks.getSongsFlow(order).collect { loadedTracks ->
                if (loadedTracks.isEmpty()) {
                    _uiState.update { it.copy(isLoading = false, isEmpty = true) }
                } else {
                    currentMediaItems = loadedTracks.toMediaItems()
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isEmpty = false,
                            tracks = loadedTracks,
                            sortOrder = order
                        )
                    }
                }
            }

        }
    }
}

