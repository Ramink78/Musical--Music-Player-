package rk.ui.songs

import androidx.core.net.toUri
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
import rk.core.ALBUM_ID
import rk.core.SONG_DURATION
import rk.core.SortOrder
import rk.core.buildSongMediaItem
import rk.core.player.MusicalRemote
import rk.domain.SongsUseCase
import rk.domain.model.Track
import javax.inject.Inject

@HiltViewModel
class SongsScreenViewModel
@Inject
constructor(
    private val songsUseCase: SongsUseCase,
    private val musicalRemote: MusicalRemote
) : ViewModel() {
    private val _uiState = MutableStateFlow(SongsScreenUiModel())
    val uiState = _uiState.asStateFlow()

    private var currentMediaItems = emptyList<MediaItem>()

    val playingSongFlow = musicalRemote.playingMediaItemFlow.onEach {
        it?.let { mediaItem ->
            _uiState.update { uiModel ->
                uiModel.copy(
                    currentTrack = Track(
                        id = mediaItem.mediaId,
                        albumName = mediaItem.mediaMetadata.albumTitle.toString(),
                        artist = mediaItem.mediaMetadata.artist.toString(),
                        duration = mediaItem.mediaMetadata.extras?.getLong(SONG_DURATION) ?: 0L,
                        albumId = mediaItem.mediaMetadata.extras?.getString(ALBUM_ID) ?: "",
                        songUri = mediaItem.localConfiguration?.uri.toString(),
                        title = mediaItem.mediaMetadata.artist.toString()
                    )
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
    fun loadSongs(order: SortOrder = SortOrder.DateAddedDesc) {
        _uiState.update { it.copy(isLoading = true) }
        viewModelScope.launch {
            val loadedSongs = songsUseCase.loadSongs(order = order)
            if (loadedSongs.isEmpty()) {
                _uiState.update { it.copy(isLoading = false, isEmpty = true) }
            } else {
                currentMediaItems = loadedSongs.map { track ->
                    buildSongMediaItem(
                        songId = track.id,
                        albumId = track.albumId,
                        albumName = track.albumName,
                        songUri = track.songUri.toUri(),
                        artist = track.artist,
                        duration = track.duration,
                        title = track.title,
                        coverUri = track.coverUri?.toUri()
                    )
                }
                _uiState.update {
                    it.copy(
                        isLoading = false,
                        isEmpty = false,
                        tracks = loadedSongs,
                        sortOrder = order
                    )
                }
            }
        }
    }
}

