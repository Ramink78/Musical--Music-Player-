package rk.ui.nowplaying.collapsed

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import rk.core.player.MusicalRemote
import javax.inject.Inject

@HiltViewModel
class MiniNowPlayingViewModel
@Inject
constructor(
    private val musicalRemote: MusicalRemote
) :
    ViewModel() {
    val uiState =
        combine(
            musicalRemote.isPlayingFlow,
            musicalRemote.playingMediaItemFlow
        ) { isPlaying, playingMediaItem ->
            MiniNowPlayingState(
                isVisible = playingMediaItem != null,
                isPlaying = isPlaying,
                title = playingMediaItem?.mediaMetadata?.title?.toString() ?: "",
                coverUri = playingMediaItem?.mediaMetadata?.artworkUri.toString()
            )
        }.stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = MiniNowPlayingState()
        )

    fun togglePlay() = musicalRemote.togglePlay()
}

