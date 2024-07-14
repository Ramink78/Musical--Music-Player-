package rk.musical.albums

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import rk.core.component.AlbumPlaceholder
import rk.core.component.CoverImage
import rk.core.component.Loading
import rk.core.component.coverImageOriginalSize
import rk.musical.data.model.Album

@Composable
fun AlbumsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(0.dp)
) {
    val viewModel: AlbumsScreenViewModel = hiltViewModel()
    val uiState = viewModel.uiState
    val albums = viewModel.albums
    LaunchedEffect(Unit) {
        viewModel.fetchAlbums()
    }
    Box(modifier = modifier) {
        when (uiState) {
            AlbumsScreenUiState.Loading -> {
                Loading(
                    modifier = Modifier
                        .size(20.dp)
                        .align(Alignment.Center)
                )
            }

            is AlbumsScreenUiState.Loaded -> {
                AlbumsList(
                    albums = albums,
                    modifier = modifier,
                    onAlbumClicked = {},
                    contentPadding = contentPadding
                )
            }

            is AlbumsScreenUiState.Empty -> {
                // Empty Screen
            }

        }
    }
}

@Composable
internal fun AlbumsList(
    albums: List<Album>,
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues(),
    onAlbumClicked: (Album) -> Unit
) {
    LazyVerticalGrid(
        modifier = modifier,
        columns = GridCells.Fixed(2),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = contentPadding
    ) {
        items(
            items = albums,
            key = { it.id }
        ) {
            AlbumItem(
                onClick = {
                    onAlbumClicked(it)
                },
                title = it.title,
                subtitle = it.artist,
                coverUri = it.coverUri
            )
        }
    }
}

@Composable
internal fun AlbumItem(
    onClick: () -> Unit,
    title: String,
    subtitle: String,
    coverUri: String?,
    modifier: Modifier = Modifier
) {
    ElevatedCard(
        onClick = onClick,
        modifier = modifier
    ) {
        Box {
            CoverImage(
                coverUri = coverUri,
                modifier =
                Modifier
                    .fillMaxSize()
                    .height(200.dp)
                    .clip(
                        RoundedCornerShape(8.dp)
                    ),
                placeholder = { AlbumPlaceholder() },
                size = coverImageOriginalSize
            )
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(90.dp)
                    .background(
                        brush = Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                MaterialTheme.colorScheme.surface
                            ),
                        )
                    )
                    .padding(8.dp)
                    .align(Alignment.BottomStart),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.padding(bottom = 4.dp),
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodyMedium,
                    maxLines = 1,
                    color = MaterialTheme.colorScheme.onSurface,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Preview
@Composable
private fun AlbumItemPreview() {
    AlbumItem(
        onClick = { /*TODO*/ },
        title = "Album Title",
        subtitle = "Album Artist",
        coverUri = ""
    )
}
