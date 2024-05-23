package rk.musical.ui.screen

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.launch
import rk.core.SortOrder
import rk.domain.AlbumsUseCase
import rk.domain.model.Album
import rk.musical.data.model.Song

@HiltViewModel
class AlbumsScreenViewModel
@Inject
constructor(
    private val albumsUseCase: AlbumsUseCase
) : ViewModel() {
    var uiState: AlbumsScreenUiState by mutableStateOf(AlbumsScreenUiState.Empty)
        private set
    var albums: List<Album> by mutableStateOf(emptyList())
    var albumChildren: List<Song> by mutableStateOf(emptyList())
    private var currentAlbums = emptyList<Album>()

    init {
        viewModelScope.launch {
            uiState = AlbumsScreenUiState.Loading
            val loadedAlbums = albumsUseCase.loadAlbums(SortOrder.DateAddedDesc).map {
                Album(
                    id = it.id,
                    title = it.title,
                    artist = it.artist,
                    songsCount = it.songsCount,
                    coverUri = it.coverUri
                )
            }
            albums = loadedAlbums
            println("Albums: $loadedAlbums")
            uiState =
                if (loadedAlbums.isEmpty()) {
                    AlbumsScreenUiState.Empty
                } else {
                    currentAlbums = loadedAlbums
                    AlbumsScreenUiState.Loaded
                }
        }
    }

    fun refreshAlbums() {
        viewModelScope.launch {
            uiState = AlbumsScreenUiState.Loading
            albumsUseCase.loadAlbums(SortOrder.DateAddedDesc)
            uiState = AlbumsScreenUiState.Loaded
        }
    }
}

sealed interface AlbumsScreenUiState {
    object Loaded : AlbumsScreenUiState
    object LoadedChildren : AlbumsScreenUiState
    object Loading : AlbumsScreenUiState
    object NavigateBack : AlbumsScreenUiState
    object Empty : AlbumsScreenUiState
}
