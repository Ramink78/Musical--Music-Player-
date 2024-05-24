package rk.ui.nowplaying.expanded

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Favorite
import androidx.compose.material.icons.rounded.FavoriteBorder
import androidx.compose.material.icons.rounded.Lyrics
import androidx.compose.material.icons.rounded.Speed
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.MenuDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import rk.core.PlaybackSpeed

@Composable
internal fun PlayerActions(
    modifier: Modifier,
    isFavorite: Boolean,
    onFavoriteClick: () -> Unit,
    onLyricClicked: () -> Unit,
    playbackSpeed: PlaybackSpeed,
    onPlaybackSpeedChange: (PlaybackSpeed) -> Unit
) {
    Row(
        modifier = modifier
            .fillMaxWidth(),
        horizontalArrangement = Arrangement.End

    ) {
        FavoriteButton(onClick = onFavoriteClick, isFavorite = isFavorite)
        Spacer(modifier = Modifier.weight(1f))
        LyricButton(onClick = onLyricClicked)
        PlaybackSpeedMenu(playbackSpeed = playbackSpeed, onChange = onPlaybackSpeedChange)
    }
}

@Composable
internal fun PlaybackSpeedMenu(
    modifier: Modifier = Modifier,
    playbackSpeed: PlaybackSpeed,
    onChange: (PlaybackSpeed) -> Unit
) {
    var isExpanded by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        DropdownMenu(expanded = isExpanded, onDismissRequest = { isExpanded = false }) {
            PlaybackSpeed.entries.forEach {
                DropdownMenuItem(
                    text = {
                        Text(text = it.displayName)
                    },
                    colors =
                    MenuDefaults.itemColors(
                        textColor =
                        if (playbackSpeed == it)
                            MaterialTheme.colorScheme.primary
                        else
                            Color.Unspecified
                    ),
                    onClick = { onChange(it) },
                )
            }

        }

        IconButton(onClick = { isExpanded = true }, modifier = modifier) {
            Icon(
                imageVector = Icons.Rounded.Speed,
                tint = MaterialTheme.colorScheme.onSurface,
                contentDescription = ""
            )
        }
    }


}

@Composable
internal fun LyricButton(
    modifier: Modifier = Modifier,
    onClick: () -> Unit
) {
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = Icons.Rounded.Lyrics,
            tint = MaterialTheme.colorScheme.onSurface,
            contentDescription = ""
        )
    }
}

@Composable
internal fun FavoriteButton(
    modifier: Modifier = Modifier,
    isFavorite: Boolean = false,
    onClick: () -> Unit,
) {
    val icon = if (isFavorite) Icons.Rounded.Favorite else Icons.Rounded.FavoriteBorder
    IconButton(onClick = onClick, modifier = modifier) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Preview
@Composable
private fun PlayerActionsPreview() {

}