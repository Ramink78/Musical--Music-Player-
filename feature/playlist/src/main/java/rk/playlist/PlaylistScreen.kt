package rk.playlist

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBars
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import rk.core.Padding
import rk.core.component.CoverImage
import rk.core.component.PlaylistPlaceholder
import rk.core.component.coverImageThumbnailSize
import rk.model.Playlist

@Composable
fun PlaylistScreen(
    modifier: Modifier = Modifier,
    onClick: (playlistId: Long) -> Unit
) {
    val viewModel: PlaylistScreenViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    LaunchedEffect(key1 = Unit) {
        viewModel.loadPlaylists()
    }
    Playlists(
        modifier = modifier,
        playlists = uiState.playlists,
        onClick = onClick
    )
}

@Composable
internal fun Playlists(
    modifier: Modifier,
    playlists: List<Playlist>,
    onClick: (playlistId: Long) -> Unit
) {
    LazyColumn(
        modifier = modifier,
        contentPadding = WindowInsets.statusBars.asPaddingValues()
    ) {
        items(
            items = playlists,
            key = { it.id }
        ) {
            PlaylistItem(playlist = it, onClick = onClick)
        }
    }
}

@Composable
internal fun PlaylistItem(
    modifier: Modifier = Modifier,
    coverUri: String? = null,
    playlist: Playlist,
    onClick: (playlistId: Long) -> Unit
) {
    Row(
        modifier = modifier
            .clickable {
                onClick(playlist.id.toLong())
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoverImage(
            coverUri = coverUri, size = coverImageThumbnailSize,
            placeholder = {
                  PlaylistPlaceholder()
            },
            modifier = Modifier
                .width(60.dp)
                .height(60.dp)
                .padding(Padding.xs)
                .clip(CircleShape)

        )
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = Padding.s)
        ) {
            Text(text = playlist.title)
        }

    }

}

@Preview
@Composable
private fun PlaylistItemPreview() {
    PlaylistItem(
        coverUri = "",
        onClick = {},
        playlist = Playlist(id = 5147, title = "quod", createdAt = "auctor")
    )

}