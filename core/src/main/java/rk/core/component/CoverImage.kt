package rk.core.component

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size
import kotlin.math.roundToInt

@Composable
fun CoverImage(
    coverUri: String?,
    modifier: Modifier = Modifier,
    size: Size,
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

val coverImageThumbnailSize = Size(
    width = 90.dp.value.roundToInt(),
    height = 90.dp.value.roundToInt()
)
val coverImageOriginalSize = Size.ORIGINAL