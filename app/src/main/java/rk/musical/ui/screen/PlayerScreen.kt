
package rk.musical.ui.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import coil.compose.SubcomposeAsyncImage
import coil.request.ImageRequest
import coil.size.Size

@Composable
fun CoverImage(
    coverUri: String?,
    modifier: Modifier = Modifier,
    size: Size = Size.ORIGINAL,
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
