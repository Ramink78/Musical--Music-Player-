package rk.ui.nowplaying.expanded

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import rk.core.PlaybackSpeed
import rk.core.PlayerState

@Composable
fun FullNowPlaying(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    onFavoriteClick: () -> Unit,
    onLyricClick: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onTogglePlay: () -> Unit,
    onShuffleClick: () -> Unit,
    onRepeatModeClick: () -> Unit,
    onPositionChanged: (Long) -> Unit,
    onPlaybackSpeedChange: (PlaybackSpeed) -> Unit
) {
    FullNowPlaying(modifier = modifier, trackInfo = {
        TrackInfo(
            trackName = playerState.trackName,
            artistName = playerState.trackArtist,
            coverUri = playerState.coverUri,
            modifier = Modifier.fillMaxWidth()
        )
    }, playerActions = {
        PlayerActions(
            modifier = Modifier.fillMaxWidth(),
            isFavorite = playerState.isFavorite,
            onFavoriteClick = onFavoriteClick,
            onLyricClicked = onLyricClick,
            onPlaybackSpeedChange = onPlaybackSpeedChange,
            playbackSpeed = playerState.playbackSpeed
        )
    }, playerController = {
        PlayerController(
            playerState = playerState,
            onSkipNext = onSkipNext,
            onSkipPrevious = onSkipPrevious,
            onTogglePlay = onTogglePlay,
            onShuffleClick = onShuffleClick,
            onRepeatModeClick = onRepeatModeClick,
            onPositionChanged = onPositionChanged
        )
    })


}

@Composable
internal fun FullNowPlaying(
    modifier: Modifier,
    trackInfo: @Composable () -> Unit,
    playerActions: @Composable () -> Unit,
    playerController: @Composable () -> Unit

) {
    Column(modifier = modifier) {
        trackInfo()
        playerActions()
        playerController()
    }
}

@Preview
@Composable
internal fun FullNowPlayingPreview() {
    val playerState = PlayerState.idle
    FullNowPlaying(trackInfo = {
        TrackInfo(trackName = "TrackName", artistName = "Artist", coverUri = "")
    }, playerActions = {
        PlayerActions(
            modifier = Modifier.fillMaxWidth(),
            isFavorite = true,
            onFavoriteClick = { },
            onLyricClicked = {},
            onPlaybackSpeedChange = {},
            playbackSpeed = PlaybackSpeed.Normal
        )
    }, playerController = {
        PlayerController(playerState = playerState,
            onSkipNext = { /*TODO*/ },
            onSkipPrevious = { /*TODO*/ },
            onTogglePlay = { /*TODO*/ },
            onShuffleClick = { /*TODO*/ },
            onRepeatModeClick = { /*TODO*/ }) {

        }
    }, modifier = Modifier.fillMaxSize()
    )
}