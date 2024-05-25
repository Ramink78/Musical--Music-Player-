package rk.ui.songs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.toImmutableList
import rk.core.Padding
import rk.core.Sizes
import rk.core.SortOrder
import rk.core.component.CoverImage
import rk.core.component.Loading
import rk.core.component.TrackPlaceholder
import rk.core.component.coverImageThumbnailSize
import rk.domain.model.Track

@Composable
fun SongsScreen(modifier: Modifier = Modifier) {
    val viewModel: SongsScreenViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.playingSongFlow.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.loadSongs()
    }
    SongsScreen(
        songsScreenState = uiState,
        onTrackClick = viewModel::playSong,
        modifier = modifier
    )


}

@Composable
internal fun SongsScreen(
    modifier: Modifier = Modifier,
    songsScreenState: SongsScreenUiModel,
    onTrackClick: (Int) -> Unit
) {
    Box(modifier = modifier) {
        when {
            songsScreenState.isEmpty -> {}
            songsScreenState.isLoading ->
                Loading(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center)
                )

            else -> {
                SongsList(
                    tracks =
                    songsScreenState.tracks.toImmutableList(),
                    playingTrackId = songsScreenState.currentTrack?.id,
                    onTrackClick = onTrackClick
                )
            }
        }
    }
}

@Composable
fun SongsList(
    tracks: ImmutableList<Track>,
    modifier: Modifier = Modifier,
    playingTrackId: String?,
    onTrackClick: (Int) -> Unit,
    onOrder: (SortOrder) -> Unit = {},
    initialOrder: SortOrder = SortOrder.DateAddedDesc
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
        contentPadding = WindowInsets.statusBars.asPaddingValues()

    ) {
        item {
//            OrderSelector(
//                modifier = Modifier.fillMaxWidth(),
//                onChanged = onOrder,
//                initialOrder = initialOrder
//            )
        }
        itemsIndexed(
            items = tracks,
            key = { _, track ->
                track.id
            }
        ) { index, track ->
            TrackItem(
                track = track,
                onTrackClick = { onTrackClick(index) },
                isPlaying = track.id == playingTrackId
            )
        }
    }
}

@Composable
fun TrackItem(
    track: Track,
    onTrackClick: () -> Unit,
    modifier: Modifier = Modifier,
    isPlaying: Boolean = false
) {
    val cardBackgroundColor by animateColorAsState(
        targetValue =
        if (isPlaying) {
            MaterialTheme.colorScheme.primary
        } else {
            MaterialTheme.colorScheme.surfaceVariant
        },
        label = "",
    )
    val cardContentColor by animateColorAsState(
        targetValue =
        if (isPlaying) {
            contentColorFor(
                MaterialTheme.colorScheme.primary
            )
        } else {
            contentColorFor(MaterialTheme.colorScheme.surfaceVariant)
        },
        label = "",
    )
    val cardScale by animateFloatAsState(
        targetValue = if (isPlaying) .95f else 1f,
        label = "",
    )

    Card(
        modifier =
        modifier
            .graphicsLayer {
                scaleY = cardScale
                scaleX = cardScale
            },
        onClick = onTrackClick,
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier =
            Modifier
                .fillMaxWidth()
                .drawBehind {
                    drawRect(color = cardBackgroundColor)
                }
                .padding(horizontal = 12.dp, vertical = 6.dp)

        ) {
            CoverImage(
                coverUri = track.coverUri,
                modifier =
                Modifier
                    .size(48.dp)
                    .clip(CircleShape),
                size = coverImageThumbnailSize,
                placeholder = { TrackPlaceholder() }
            )
            Column(
                modifier =
                Modifier
                    .weight(1f)
                    .padding(Padding.s),
                verticalArrangement = Arrangement.Center
            ) {
                Text(
                    text = track.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontSize = Sizes.mediumText,
                    modifier = Modifier.padding(bottom = Padding.xs),
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = cardContentColor
                )
                Text(
                    text = track.artist,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    fontSize = Sizes.smallText,
                    overflow = TextOverflow.Ellipsis,
                    color = cardContentColor
                )
            }
        }
    }
}

@Preview
@Composable
private fun SongsScreenPreview() {
    val state =
        SongsScreenUiModel(
            isLoading = false,
            isEmpty = false,
            tracks = listOf(
                Track(
                    "",
                    "Title",
                    "Artist",
                    "",
                    "",
                    0L,
                    "", ""
                )
            )
        )
    SongsScreen(songsScreenState = state, modifier = Modifier.fillMaxSize()) {
    }
}

