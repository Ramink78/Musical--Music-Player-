@file:OptIn(ExperimentalMaterial3Api::class)

package rk.musical.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SheetValue
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import kotlinx.coroutines.launch
import rk.core.PlaybackSpeed
import rk.core.PlayerState
import rk.core.RepeatMode
import rk.musical.ui.component.SongPlaceholder
import rk.musical.utils.NowPlayingDynamicTheme
import rk.musical.utils.verticalGradientScrim
import rk.ui.nowplaying.collapsed.MiniNowPlaying
import rk.ui.nowplaying.expanded.FullNowPlaying

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PlayerScreen(
    behindContent: @Composable (PaddingValues) -> Unit,
    sheetPeakHeight: Dp = 0.dp,
    sheetState: BottomSheetScaffoldState
) {
    val viewModel: PlayerScreenViewModel = hiltViewModel()
    val isVisibleState by viewModel.isVisibleSheetFlow.collectAsStateWithLifecycle()
    val sheetRadius by animateDpAsState(
        targetValue =
        if (sheetState.bottomSheetState.targetValue == SheetValue.Expanded) 0.dp else 16.dp,
        label = ""
    )
    PlayerScreen(
        sheetState = sheetState,
        isSheetVisible = isVisibleState,
        sheetRadius = sheetRadius,
        sheetPeakHeight = sheetPeakHeight,
        behindContent = behindContent
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun PlayerScreen(
    sheetState: BottomSheetScaffoldState,
    isSheetVisible: Boolean,
    sheetRadius: Dp = 16.dp,
    sheetPeakHeight: Dp,
    behindContent: @Composable (PaddingValues) -> Unit
) {
    val scope = rememberCoroutineScope()
    val sheetPeekHeightAnimate by animateDpAsState(
        targetValue =
        if (isSheetVisible) sheetPeakHeight else 0.dp,
        label = ""
    )
    BackHandler(
        enabled =
        sheetState.bottomSheetState.currentValue == SheetValue.Expanded
    ) {
        scope.launch {
            sheetState.bottomSheetState.partialExpand()
        }
    }
    BottomSheetScaffold(
        sheetDragHandle = null,
        sheetShape = RoundedCornerShape(topStart = sheetRadius, topEnd = sheetRadius),
        sheetContent = {
            if (isSheetVisible) {
                Surface(modifier = Modifier.fillMaxSize()) {
                    Box {
                        Crossfade(
                            targetState = sheetState.bottomSheetState.targetValue,
                            label = ""
                        ) {
                            if (it == SheetValue.Expanded) {
                                ExpandedPlayer(
                                    modifier =
                                    Modifier
                                        .fillMaxSize()
                                )
                            } else {
                                CollapsedPlayer(
                                    Modifier.clickable {
                                        scope.launch {
                                            sheetState.bottomSheetState.expand()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }
            }
        },
        sheetTonalElevation = 0.dp,
        sheetPeekHeight = sheetPeekHeightAnimate,
        scaffoldState = sheetState
    ) {
        behindContent(it)
    }
}

@Composable
private fun CollapsedPlayer(modifier: Modifier = Modifier) {
    val viewModel: CollapsedNowPlayingViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    MiniNowPlaying(
        coverUri = uiState.playingSong.coverUri,
        title = uiState.playingSong.title,
        onTogglePlay = viewModel::togglePlay,
        isPlaying = uiState.isPlaying,
        coverPlaceholder = { SongPlaceholder() },
        backgroundColor = MaterialTheme.colorScheme.surface,
        modifier = modifier
    )
}

@Composable
private fun ExpandedPlayer(modifier: Modifier = Modifier) {
    val viewModel: ExpandedNowPlayingViewModel = hiltViewModel()
    val uiState = viewModel.nowPlayingUiStateFlow.collectAsStateWithLifecycle()
    val currentLyric by viewModel.currentLyric.collectAsStateWithLifecycle()
    val lyricUiState by viewModel.lyricUiState.collectAsStateWithLifecycle()
    val repeatMode by viewModel.repeatModeFlow.collectAsStateWithLifecycle()
    val isShuffleMode by viewModel.shuffleModeFlow.collectAsStateWithLifecycle()

    val playerState = PlayerState(
        isPlaying = uiState.value.isPlaying,
        coverUri = uiState.value.currentSong.coverUri,
        passedTime = uiState.value.currentTime,
        totalTime = uiState.value.totalTime,
        isFavorite = uiState.value.isFavorite,
        repeatMode = RepeatMode.One,
        shuffleMode = isShuffleMode,
        trackName = uiState.value.currentSong.title,
        trackArtist = uiState.value.currentSong.artist,
        playbackSpeed = PlaybackSpeed.Normal
    )

    NowPlayingDynamicTheme(coverUri = uiState.value.currentSong.coverUri ?: "") {
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
                FullNowPlaying(
                    modifier = modifier,
                    playerState = playerState,
                    onFavoriteClick = {},
                    onLyricClick = { },
                    onSkipNext = viewModel::skipToNext,
                    onSkipPrevious = viewModel::skipToPrevious,
                    onTogglePlay = viewModel::togglePlay,
                    onShuffleClick = viewModel::toggleShuffleMode,
                    onRepeatModeClick = viewModel::changeRepeatMode,
                    onPositionChanged = viewModel::seekToProgress,
                    onPlaybackSpeedChange = viewModel::setPlaybackSpeed
                )
            }
        }
    }
}

@Composable
fun CoverImage(
    coverUri: String?,
    modifier: Modifier = Modifier,
    size: Size = Size.ORIGINAL,
    placeholder: @Composable () -> Unit
) {
    val context = LocalContext.current
    SubcomposeAsyncImage(
        model =
        ImageRequest.Builder(context)
            .data(coverUri)
            .size(size = size)
            .build(),
        contentScale = ContentScale.Crop,
        modifier = modifier,
        contentDescription = "",
        error = {
            placeholder()
        }
    )
}
