package rk.ui.nowplaying.expanded

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import rk.core.Padding
import rk.core.Sizes
import rk.core.component.CoverImage
import rk.core.component.TrackPlaceholder
import rk.core.component.coverImageOriginalSize

@Composable
internal fun TrackInfo(
    modifier: Modifier = Modifier,
    trackName: String,
    artistName: String,
    coverUri: String?,
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        CoverImage(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .padding(horizontal = Padding.s)
                .statusBarsPadding()
                .clip(RoundedCornerShape(10.dp)),
            coverUri = coverUri,
            size = coverImageOriginalSize,
            placeholder = {
                TrackPlaceholder()
            }
        )
        Text(
            text = trackName,
            modifier = Modifier.padding(bottom = Padding.xs, top = Padding.s),
            style = MaterialTheme.typography.headlineLarge.copy(fontSize = Sizes.mediumText),
            maxLines = 2,
            overflow = TextOverflow.Ellipsis
        )
        Text(
            text = artistName,
            style = MaterialTheme.typography.labelMedium.copy(fontSize = Sizes.smallText),
            maxLines = 1
        )

    }
}