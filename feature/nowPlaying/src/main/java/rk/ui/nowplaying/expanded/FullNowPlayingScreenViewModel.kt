package rk.ui.nowplaying.expanded

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.Player
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import rk.core.PlaybackSpeed
import rk.core.PlayerState
import rk.core.RepeatMode
import rk.core.SONG_DURATION
import rk.core.player.MusicalRemote
import rk.core.readableDuration
import javax.inject.Inject

@HiltViewModel
class FullNowPlayingScreenViewModel
@Inject
constructor(
    private val musicalRemote: MusicalRemote,
) : ViewModel() {
    val uiState = combine(
        flow = musicalRemote.playingMediaItemFlow,
        flow2 = musicalRemote.shuffleModeFlow,
        flow3 = musicalRemote.isPlayingFlow,
        flow4 = musicalRemote.currentPositionFlow,
        flow5 = musicalRemote.repeatModeFlow
    ) { playingMediaItem, isShuffleMode, isPlaying, currentPosition, repeatMode ->
        val totalMillis = playingMediaItem?.mediaMetadata?.extras?.getLong(
            SONG_DURATION,
            0L
        ) ?: 1L
        val progress = currentPosition / totalMillis.toFloat()
        FullNowPlayingScreenState(
            playerState = PlayerState(
                isPlaying = isPlaying,
                trackName = playingMediaItem?.mediaMetadata?.title.toString(),
                trackArtist = playingMediaItem?.mediaMetadata?.artist.toString(),
                coverUri = playingMediaItem?.mediaMetadata?.artworkUri.toString(),
                totalTime = readableDuration(
                    totalMillis
                ),
                passedTime = readableDuration(currentPosition),
                shuffleMode = isShuffleMode,
                repeatMode = repeatMode,
                isFavorite = false,
                playbackSpeed = PlaybackSpeed.Normal,
                progress = progress
            ),
            isLyricMode = false
        )
    }
        .stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = FullNowPlayingScreenState()
        )

    fun skipToNext() = musicalRemote.seekNext()

    fun skipToPrevious() = musicalRemote.seekPrevious()

    fun togglePlay() = musicalRemote.togglePlay()

    fun toggleShuffleMode() {
        musicalRemote.setShuffleMode(!uiState.value.playerState.shuffleMode)
    }

    fun seekToProgress(progress: Float) {
        musicalRemote.seekToPosition(progress)
    }

    fun changeRepeatMode() {
        when (uiState.value.playerState.repeatMode) {
            RepeatMode.Off -> musicalRemote.setRepeatMode(Player.REPEAT_MODE_ALL)
            RepeatMode.All -> musicalRemote.setRepeatMode(Player.REPEAT_MODE_ONE)
            RepeatMode.One -> musicalRemote.setRepeatMode(Player.REPEAT_MODE_OFF)
        }
    }

    fun setPlaybackSpeed(playbackSpeed: PlaybackSpeed) {
        musicalRemote.setPlaybackSpeed(
            when (playbackSpeed) {
                PlaybackSpeed.Slow -> .5f
                PlaybackSpeed.Normal -> 1f
                PlaybackSpeed.Fast -> 1.5f
                PlaybackSpeed.VeryFast -> 2f
            }
        )
    }
}
