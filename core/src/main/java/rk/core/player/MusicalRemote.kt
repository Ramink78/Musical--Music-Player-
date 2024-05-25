package rk.core.player

import android.animation.ValueAnimator
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.animation.addListener
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.RepeatMode
import androidx.media3.exoplayer.ExoPlayer
import dagger.hilt.android.scopes.ActivityRetainedScoped
import kotlinx.coroutines.flow.combine
import javax.inject.Inject

@ActivityRetainedScoped
class MusicalRemote
@Inject
constructor(private val exoPlayer: ExoPlayer) {
    val playingMediaItemFlow = exoPlayer.playingMediaItemFlow()
    val isPlayingFlow = exoPlayer.isPlayingFlow()
    val currentPositionFlow = exoPlayer.currentPositionFlow()
    val repeatModeFlow = exoPlayer.repeatModeFlow()
    val shuffleModeFlow = exoPlayer.shuffleModeFlow()
    var currentPlaylist = emptyList<MediaItem>()
        private set
    val playbackStateFlow =
        combine(
            playingMediaItemFlow,
            isPlayingFlow,
            currentPositionFlow
        ) { playingMediaItem, isPlaying, currentPosition ->
            MusicalRemoteState(
                isPlaying = isPlaying,
                currentMediaItem = playingMediaItem,
                currentPosition = currentPosition
            )
        }

    fun playSong(index: Int) {
        exoPlayer.seekToDefaultPosition(index)
        exoPlayer.playWhenReady = true
    }

    fun setPlaylist(mediaItems: List<MediaItem>) {
        currentPlaylist = mediaItems
        exoPlayer.setMediaItems(mediaItems)
        exoPlayer.prepare()
    }

    private fun pause() {
        smoothFadeOut(onEnd = {
            exoPlayer.playWhenReady = false
            // change volume to default (max volume)
            exoPlayer.volume = 1f
        })
    }

    private fun play() {
        smoothFadeIn(onStart = {
            exoPlayer.playWhenReady = true
        })
    }

    private fun smoothFadeOut(onEnd: () -> Unit) {
        ValueAnimator.ofFloat(1f, 0f)
            .apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = 400
                addListener(onEnd = { onEnd() })
                addUpdateListener {
                    exoPlayer.volume = it.animatedValue as Float
                }
                start()
            }
    }

    private fun smoothFadeIn(onStart: () -> Unit) {
        ValueAnimator.ofFloat(0f, 1f)
            .apply {
                interpolator = AccelerateDecelerateInterpolator()
                duration = 400
                addListener(onStart = { onStart() })
                addUpdateListener {
                    exoPlayer.volume = it.animatedValue as Float
                }
                start()
            }
    }

    fun togglePlay() {
        if (exoPlayer.isPlaying) {
            pause()
        } else {
            play()
        }
    }

    fun seekNext() = exoPlayer.seekToNext()

    fun seekPrevious() = exoPlayer.seekToPrevious()

    fun seekToPosition(pos: Long) = exoPlayer.seekTo(pos)

    fun setRepeatMode(
        @RepeatMode repeatMode: Int
    ) {
        exoPlayer.repeatMode = repeatMode
    }

    fun setShuffleMode(isShuffleMode: Boolean) {
        exoPlayer.shuffleModeEnabled = isShuffleMode
    }

    fun setPlaybackSpeed(speed: Float) = exoPlayer.setPlaybackSpeed(speed)
}

data class MusicalRemoteState(
    val isPlaying: Boolean = false,
    val currentMediaItem: MediaItem? = null,
    val currentPosition: Long = 0L
)
