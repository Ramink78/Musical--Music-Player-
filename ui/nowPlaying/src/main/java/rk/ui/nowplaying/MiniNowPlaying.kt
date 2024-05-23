package rk.ui.nowplaying

import androidx.compose.foundation.background
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import kotlin.math.roundToInt

@Composable
fun MiniNowPlaying(
    modifier: Modifier = Modifier,
    coverUri: String?,
    title: String,
    onTogglePlay: () -> Unit,
    isPlaying: Boolean,
    coverPlaceholder: @Composable () -> Unit,
    radius: Dp = 16.dp,
    backgroundColor: Color
) {
    Row(
        modifier =
        modifier
            .fillMaxWidth()
            .background(
                backgroundColor,
                shape = RoundedCornerShape(topStart = radius, topEnd = radius)
            )
            .padding(horizontal = 8.dp)
            .height(64.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        CoverImage(
            coverUri = coverUri,
            modifier =
            Modifier
                .size(48.dp)
                .clip(CircleShape),
            size = Size(width = 128, 128),
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

@Composable
fun CoverImage(
    coverUri: String?,
    modifier: Modifier = Modifier,
    size: Size = Size(
        width = 48.dp.value.roundToInt(),
        height = 48.dp.value.roundToInt()
    ),
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

@Preview
@Composable
private fun MiniNowPlayingPrevieww() {
    MiniNowPlaying(
        coverUri = "",
        title = "Title",
        onTogglePlay = { /*TODO*/ },
        isPlaying = false,
        coverPlaceholder = {},
        backgroundColor = Color.White
    )

}