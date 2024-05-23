package rk.musical.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import rk.core.SortOrder
import rk.domain.SongsUseCase
import rk.musical.data.model.Song
import rk.musical.player.MusicalRemote

@HiltViewModel
class SongsScreenViewModel
@Inject
constructor(
    private val songsUseCase: SongsUseCase,
    private val musicalRemote: MusicalRemote
) : ViewModel() {
    private val _uiState = MutableStateFlow<SongsScreenUiState>(SongsScreenUiState.Loading)
    val uiState = _uiState.asStateFlow()

    private var currentSongs = emptyList<Song>()

    val playingSongFlow =
        musicalRemote.playingSongFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Song.Empty
            )

    fun playSong(index: Int) {
        if (musicalRemote.currentPlaylist != currentSongs) {
            musicalRemote.setPlaylist(currentSongs)
            //    hasCurrentPlaylist = true
        }
        musicalRemote.playSong(index)
    }
    fun loadSongs(order: SortOrder = SortOrder.DateAddedDesc) {
        viewModelScope.launch {
            val loadedSongs = songsUseCase.loadSongs(order = order).map {
                Song(
                    id = it.id,
                    albumId = it.albumId,
                    albumName = it.albumName,
                    songUri = it.songUri,
                    artist = it.artist,
                    duration = it.duration,
                    title = it.title,
                    coverUri = it.coverUri
                )
            }
            if (loadedSongs.isEmpty()) {
                _uiState.update { SongsScreenUiState.Empty }
            } else {
                currentSongs = loadedSongs
                _uiState.update { SongsScreenUiState.Loaded(loadedSongs, order) }
            }
        }
    }
}

sealed interface SongsScreenUiState {
    data class Loaded(val songs: List<Song>, val order: SortOrder) : SongsScreenUiState
    data object Loading : SongsScreenUiState
    data object Empty : SongsScreenUiState
}
