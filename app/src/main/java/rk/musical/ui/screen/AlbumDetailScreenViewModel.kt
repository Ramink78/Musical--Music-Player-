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
import rk.core.player.MusicalRemote
import rk.domain.model.Album
import rk.musical.data.model.Song
import rk.musical.domain.GetAllTracks

@HiltViewModel
class AlbumDetailScreenViewModel
@Inject
constructor(
    private val getAllSongsUseCase: GetAllTracks,
    private val musicalRemote: MusicalRemote
) : ViewModel() {
    private val _uiState = MutableStateFlow<AlbumDetailUiState>(AlbumDetailUiState.Loading)
    val uiState = _uiState.asStateFlow()
    private var currentSongs = emptyList<Song>()
    val playingSong =
        musicalRemote.playingMediaItemFlow
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = Song.Empty
            )

    fun getAlbumChildren(albumId: String) {
        _uiState.update { AlbumDetailUiState.Loading }
     /*   viewModelScope.launch {
            val children = getAllSongsUseCase.getAlbumSongs(albumId)
            _uiState.update {
                val child = children.first()
                AlbumDetailUiState.Loaded(
                    album = Album(
                        id = child.albumId,
                        title = child.albumName,
                        artist = child.artist,
                        coverUri = child.coverUri,
                        songsCount = children.size
                    ),
                    children = children.map {
                        Song(
                            id = it.id,
                            title = it.title,
                            coverUri = it.coverUri,
                            artist = it.artist,
                            albumId = it.albumId,
                            albumName = it.albumName,
                            duration = it.duration,
                            songUri = it.songUri
                        )
                    }.also { currentSongs = it }
                )
            }
        }*/
    }

    fun playSong(index: Int) {
        if (musicalRemote.currentPlaylist != currentSongs) {
            // musicalRemote.setPlaylist(currentSongs)
            // hasCurrentPlaylist = true
        }
        musicalRemote.playSong(index)
    }
}

sealed interface AlbumDetailUiState {
    data object Loading : AlbumDetailUiState
    data class Loaded(
        val album: Album,
        val children: List<Song>
    ) : AlbumDetailUiState
}
