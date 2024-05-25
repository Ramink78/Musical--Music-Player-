package rk.musical.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import rk.core.player.MusicalRemote
import rk.musical.data.model.Song

@HiltViewModel
class CollapsedNowPlayingViewModel
@Inject
constructor(
    private val musicalRemote: MusicalRemote
) :
    ViewModel() {
    val uiState =
        combine(
            musicalRemote.isPlayingFlow,
            musicalRemote.playingMediaItemFlow
        ) { isPlaying, playingSong ->
            CollapsedNowPlayingUiState(
                isPlaying = isPlaying
                // playingSong = playingSong
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = CollapsedNowPlayingUiState()
        )

    fun togglePlay() = musicalRemote.togglePlay()
}

data class CollapsedNowPlayingUiState(
    val isPlaying: Boolean = false,
    val playingSong: Song = Song.Empty
)
