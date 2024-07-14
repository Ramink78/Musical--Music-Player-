package rk.musical.playlist.detail

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import rk.core.component.Loading

@Composable
fun PlaylistDetailScreen(modifier: Modifier = Modifier, playlistId: Long) {
    val viewModel: PlaylistDetailScreenViewModel = hiltViewModel()
    val uiModel by viewModel.uiState.collectAsStateWithLifecycle()
    PlaylistDetailScreen(uiModel = uiModel, modifier = modifier)
    LaunchedEffect(key1 = Unit) {
        viewModel.loadPlaylistTracks(playlistId)
    }
}

@Composable
internal fun PlaylistDetailScreen(
    modifier: Modifier = Modifier,
    uiModel: PlaylistDetailScreenUiModel
) {
    if (uiModel.isLoading)
        Loading()
    else
        LazyColumn(
            modifier = modifier,
        ) {
            items(
                items = uiModel.tracks,
                key = { it.id }
            ) {
                Text(text = it.title)
            }
        }
}

@Preview
@Composable
private fun Preview() {
    PlaylistDetailScreen(playlistId = 0)
}
