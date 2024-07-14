package rk.musical.albums

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import rk.core.SortOrder
import rk.domain.AlbumsUseCase
import rk.musical.data.model.Album
import javax.inject.Inject

@HiltViewModel
class AlbumsScreenViewModel @Inject constructor(
    private val albumsUseCase: AlbumsUseCase
) : ViewModel() {
    var uiState: AlbumsScreenUiState by mutableStateOf(AlbumsScreenUiState.Empty)
        private set
    var albums: List<Album> by mutableStateOf(emptyList())
    private var currentAlbums = emptyList<Album>()

    fun fetchAlbums() {
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
            uiState =
                if (loadedAlbums.isEmpty()) {
                    AlbumsScreenUiState.Empty
                } else {
                    currentAlbums = loadedAlbums
                    AlbumsScreenUiState.Loaded
                }
        }
    }
}

sealed interface AlbumsScreenUiState {
    data object Loaded : AlbumsScreenUiState
    data object Loading : AlbumsScreenUiState
    data object Empty : AlbumsScreenUiState
}