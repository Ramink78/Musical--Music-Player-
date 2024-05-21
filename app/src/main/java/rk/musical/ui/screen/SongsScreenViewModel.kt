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
import rk.musical.data.SongRepository
import rk.musical.data.model.Song
import rk.musical.player.MusicalRemote

@HiltViewModel
class SongsScreenViewModel
@Inject
constructor(
    private val songRepository: SongRepository,
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

    fun refreshSongs() {
        viewModelScope.launch {
            songRepository.loadSongs()
        }
    }

    fun playSong(index: Int) {
        if (musicalRemote.currentPlaylist != currentSongs) {
            musicalRemote.setPlaylist(currentSongs)
            //    hasCurrentPlaylist = true
        }
        musicalRemote.playSong(index)
    }
    fun loadSongs() {
        viewModelScope.launch {
            val loadedSongs = songRepository.loadSongs()
            if (loadedSongs.isEmpty()) {
                _uiState.update { SongsScreenUiState.Empty }
            } else {
                currentSongs = loadedSongs
                _uiState.update { SongsScreenUiState.Loaded(loadedSongs) }
            }
        }
    }
}

sealed interface SongsScreenUiState {
    data class Loaded(val songs: List<Song>) : SongsScreenUiState
    data object Loading : SongsScreenUiState
    data object Empty : SongsScreenUiState
}
