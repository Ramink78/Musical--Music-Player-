package rk.ui.nowplaying.collapsed

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import rk.core.component.CoverImage
import rk.core.component.TrackPlaceholder
import rk.core.component.coverImageThumbnailSize

@Composable
fun MiniNowPlaying(
    modifier: Modifier = Modifier,
    onNavigateToFullNowPlaying: () -> Unit
) {
    val viewModel: MiniNowPlayingViewModel = hiltViewModel()
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    AnimatedVisibility(
        visible = uiState.isVisible,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it }
    ) {
        MiniNowPlaying(
            modifier = modifier,
            coverUri = uiState.coverUri,
            title = uiState.title,
            onTogglePlay = viewModel::togglePlay,
            isPlaying = uiState.isPlaying,
            coverPlaceholder = { TrackPlaceholder() },
            backgroundColor = MaterialTheme.colorScheme.surface,
            onClick = onNavigateToFullNowPlaying
        )
    }
}
@Composable
internal fun MiniNowPlaying(
    modifier: Modifier = Modifier,
    coverUri: String?,
    title: String,
    onTogglePlay: () -> Unit,
    isPlaying: Boolean,
    coverPlaceholder: @Composable () -> Unit,
    radius: Dp = 16.dp,
    backgroundColor: Color,
    onClick: () -> Unit
) {
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .clip(
                shape = RoundedCornerShape(topStart = radius, topEnd = radius)
            )
            .background(backgroundColor)
            .clickable { onClick() }
            .padding(horizontal = 8.dp)
            .height(64.dp)
            ,
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoverImage(
            coverUri = coverUri,
            modifier =
            Modifier
                .size(48.dp)
                .clip(CircleShape),
            size = coverImageThumbnailSize,
            placeholder = { coverPlaceholder() }
        )
        Spacer(modifier = Modifier.width(12.dp))
        Text(
            modifier = Modifier.weight(1f),
            text = title,
            style = MaterialTheme.typography.titleMedium,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
        )
        IconButton(onClick = onTogglePlay) {
            Icon(
                imageVector =
                if (isPlaying) {
                    Icons.Rounded.Pause
                } else {
                    Icons.Rounded.PlayArrow
                },
                contentDescription = "",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

@Preview
@Composable
internal fun MiniNowPlayingPreview() {
    MiniNowPlaying(
        coverUri = "",
        title = "Title",
        onTogglePlay = { /*TODO*/ },
        isPlaying = false,
        coverPlaceholder = {},
        backgroundColor = Color.White,
        onClick = {}
    )

}