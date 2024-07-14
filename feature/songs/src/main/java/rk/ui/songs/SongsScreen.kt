package rk.ui.songs

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material3.Card
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.contentColorFor
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.res.stringResource
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
import rk.musical.domain.model.Track

@Composable
fun SongsScreen(
    modifier: Modifier = Modifier,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp)
) {
    val viewModel: SongsScreenViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    viewModel.playingSongFlow.collectAsStateWithLifecycle()
    LaunchedEffect(Unit) {
        viewModel.loadSongs(SortOrder.DateAddedDesc)
    }
    SongsScreen(
        songsScreenState = uiState,
        onTrackClick = viewModel::playSong,
        onOrder = viewModel::loadSongs,
        modifier = modifier,
        order = uiState.sortOrder,
        contentPaddingValues = contentPaddingValues
    )


}

@Composable
internal fun SongsScreen(
    modifier: Modifier = Modifier,
    songsScreenState: SongsScreenUiModel,
    onTrackClick: (Int) -> Unit,
    onOrder: (SortOrder) -> Unit,
    order: SortOrder,
    contentPaddingValues: PaddingValues = PaddingValues(0.dp)
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
                    onTrackClick = onTrackClick,
                    onOrder = onOrder,
                    initialOrder = order,
                    contentPaddingValues = contentPaddingValues
                )
            }
        }
    }
}

@Composable
internal fun SongsList(
    tracks: ImmutableList<Track>,
    modifier: Modifier = Modifier,
    playingTrackId: Long?,
    onTrackClick: (Int) -> Unit,
    onOrder: (SortOrder) -> Unit,
    initialOrder: SortOrder,
    contentPaddingValues: PaddingValues
) {
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(4.dp),
        modifier = modifier,
        contentPadding = contentPaddingValues

    ) {
        item {
            OrderSelector(
                modifier = Modifier.fillMaxWidth(),
                onChanged = onOrder,
                initialOrder = initialOrder
            )
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

@Composable
fun OrderSelector(
    modifier: Modifier = Modifier,
    initialOrder: SortOrder,
    onChanged: (SortOrder) -> Unit
) {
    val isDateAddedSelected = initialOrder == SortOrder.DateAddedDesc ||
            initialOrder == SortOrder.DateAddedAsc
    val isNameSelected = initialOrder == SortOrder.NameAsc ||
            initialOrder == SortOrder.NameDesc
    var shouldShowDescIcon by remember {
        mutableStateOf(
            initialOrder == SortOrder.DateAddedDesc ||
                    initialOrder == SortOrder.NameDesc
        )
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        FilterChip(
            shape = CircleShape,
            selected = isDateAddedSelected,
            onClick = {
                if (initialOrder == SortOrder.DateAddedDesc) {
                    onChanged(SortOrder.DateAddedAsc)
                    shouldShowDescIcon = false
                } else {
                    onChanged(SortOrder.DateAddedDesc)
                    shouldShowDescIcon = true
                }
            },
            label = {
                Text(
                    text = stringResource(R.string.sort_selector_date_added),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingIcon = {
                if (isDateAddedSelected) {
                    if (shouldShowDescIcon) {
                        Icon(imageVector = Icons.Rounded.ArrowDownward, contentDescription = "")
                    } else {
                        Icon(imageVector = Icons.Rounded.ArrowUpward, contentDescription = "")
                    }
                }
            }
        )
        FilterChip(
            shape = CircleShape,
            selected = isNameSelected,
            onClick = {
                if (initialOrder == SortOrder.NameDesc) {
                    onChanged(SortOrder.NameAsc)
                    shouldShowDescIcon = false
                } else {
                    onChanged(SortOrder.NameDesc)
                    shouldShowDescIcon = true
                }
            },
            label = {
                Text(
                    text = stringResource(R.string.sort_selector_name),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingIcon = {
                if (isNameSelected) {
                    if (shouldShowDescIcon) {
                        Icon(imageVector = Icons.Rounded.ArrowDownward, contentDescription = "")
                    } else {
                        Icon(imageVector = Icons.Rounded.ArrowUpward, contentDescription = "")
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun OrderSelectorPreview() {
    OrderSelector(
        modifier = Modifier.fillMaxWidth(),
        initialOrder = SortOrder.DateAddedAsc
    ) {
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
                    id = 2498,
                    title = "equidem",
                    artist = "aliquid",
                    coverUri = null,
                    songUri = "rutrum",
                    albumName = "Derrick Ramirez",
                    duration = 6401,
                    albumId = "alienum"

                )
            )
        )
    SongsScreen(songsScreenState = state, modifier = Modifier.fillMaxSize(),
        onOrder = {}, onTrackClick = {}, order = SortOrder.DateAddedDesc)
}

