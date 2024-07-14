package rk.ui.nowplaying.expanded

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import rk.core.PlaybackSpeed
import rk.core.PlayerState

@Composable
fun FullNowPlayingScreen(modifier: Modifier = Modifier) {
    val viewModel: FullNowPlayingScreenViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    NowPlayingDynamicTheme(coverUri = uiState.playerState.coverUri ?: "") {
        Box {
            Column(
                modifier =
                modifier
                    .verticalGradientScrim(
                        color = MaterialTheme.colorScheme.background.copy(alpha = .5f),
                        startYPercentage = 1f,
                        endYPercentage = 0f
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                FullNowPlayingScreen(
                    modifier = modifier,
                    playerState = uiState.playerState,
                    onFavoriteClick = {},
                    onLyricClick = {},
                    onSkipNext = viewModel::skipToNext,
                    onSkipPrevious = viewModel::skipToPrevious,
                    onTogglePlay = viewModel::togglePlay,
                    onShuffleClick = viewModel::toggleShuffleMode,
                    onRepeatModeClick = viewModel::changeRepeatMode,
                    onPositionChanged = viewModel::seekToProgress,
                    onPlaybackSpeedChange = viewModel::setPlaybackSpeed,
                )
            }
        }
    }


}


@Composable
internal fun FullNowPlayingScreen(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    onFavoriteClick: () -> Unit,
    onLyricClick: () -> Unit,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onTogglePlay: () -> Unit,
    onShuffleClick: () -> Unit,
    onRepeatModeClick: () -> Unit,
    onPositionChanged: (Float) -> Unit,
    onPlaybackSpeedChange: (PlaybackSpeed) -> Unit,
) {
    FullNowPlayingScreen(modifier = modifier, trackInfo = {
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
            onPositionChanged = onPositionChanged,
        )
    })


}

@Composable
internal fun FullNowPlayingScreen(
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
    FullNowPlayingScreen(
        trackInfo = {
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
            onRepeatModeClick = { /*TODO*/ },
            onPositionChanged = {})

    }, modifier = Modifier.fillMaxSize()
    )
}