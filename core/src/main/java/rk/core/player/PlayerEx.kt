package rk.core.player

import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.channels.trySendBlocking
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import rk.core.RepeatMode

fun Player.isPlayingFlow() =
    callbackFlow {
        send(isPlaying)
        val listener =
            object : Player.Listener {
                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    trySendBlocking(isPlaying)
                }
            }
        addListener(listener)
        awaitClose { removeListener(listener) }
    }.flowOn(Dispatchers.Main)

fun Player.playingMediaItemFlow() =
    callbackFlow {
        send(currentMediaItem)
        val listener =
            object : Player.Listener {
                override fun onMediaItemTransition(
                    mediaItem: MediaItem?,
                    reason: Int
                ) {
                    if (mediaItem == null) return
                    trySendBlocking(mediaItem)
                }
            }
        addListener(listener)
        awaitClose { removeListener(listener) }
    }.flowOn(Dispatchers.Main)

// this flow emit only in FullNowPlayingScreen expanded state
fun Player.currentPositionFlow() =
    flow {
        while (true) {
            emit(currentPosition)
            delay(600)
        }
    }.flowOn(Dispatchers.Main)

fun Player.repeatModeFlow() =
    callbackFlow {
        send(mapIntRoRepeatMode(repeatMode))
        val listener =
            object : Player.Listener {
                override fun onRepeatModeChanged(@Player.RepeatMode repeatMode: Int) {
                    trySendBlocking(mapIntRoRepeatMode(repeatMode))
                }
            }
        addListener(listener)
        awaitClose { removeListener(listener) }
    }.flowOn(Dispatchers.Main)

fun Player.shuffleModeFlow() =
    callbackFlow {
        send(shuffleModeEnabled)
        val listener =
            object : Player.Listener {
                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                    trySendBlocking(shuffleModeEnabled)
                }
            }
        addListener(listener)
        awaitClose { removeListener(listener) }
    }.flowOn(Dispatchers.Main)

fun mapIntRoRepeatMode(@Player.RepeatMode repeatMode: Int): RepeatMode {
    return when (repeatMode) {
        Player.REPEAT_MODE_ALL -> {
            RepeatMode.All
        }

        Player.REPEAT_MODE_OFF -> {
            RepeatMode.Off
        }

        else -> {
            RepeatMode.One
        }
    }
}
