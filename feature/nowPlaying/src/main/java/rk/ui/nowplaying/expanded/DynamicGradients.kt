package rk.ui.nowplaying.expanded

import android.content.Context
import androidx.collection.LruCache
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.Immutable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.Stable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.compositeOver
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.platform.LocalContext
import androidx.core.graphics.drawable.toBitmap
import androidx.palette.graphics.Palette
import coil.imageLoader
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlin.math.max
import kotlin.math.min

@Composable
fun rememberDominantColorState(
    context: Context = LocalContext.current,
    defaultColor: Color = MaterialTheme.colorScheme.background,
    cacheSize: Int = 12,
    isColorValid: (Color) -> Boolean = { true }
): DominantColorState =
    remember {
        DominantColorState(context, defaultColor, cacheSize, isColorValid)
    }

/**
 * A composable which allows dynamic theming of the [androidx.compose.material.Colors.primary]
 * color from an image.
 */
@Composable
fun DynamicThemePrimaryColorsFromImage(
    dominantColorState: DominantColorState = rememberDominantColorState(),
    content: @Composable () -> Unit
) {
    val colors =
        MaterialTheme.colorScheme.copy(
            background =
            animateColorAsState(
                dominantColorState.color,
                spring(stiffness = Spring.StiffnessLow),
                label = ""
            ).value
        )
    MaterialTheme(colorScheme = colors, content = content)
}

/**
 * A class which stores and caches the result of any calculated dominant colors
 * from images.
 *
 * @param context Android context
 * @param defaultColor The default color, which will be used if [calculateDominantColor] fails to
 * calculate a dominant color
 * @param cacheSize The size of the [LruCache] used to store recent results. Pass `0` to
 * disable the cache.
 * @param isColorValid A lambda which allows filtering of the calculated image colors.
 */
@Stable
class DominantColorState(
    private val context: Context,
    private val defaultColor: Color,
    cacheSize: Int = 12,
    private val isColorValid: (Color) -> Boolean = { true }
) {
    var color by mutableStateOf(defaultColor)
        private set

    private val cache =
        when {
            cacheSize > 0 -> LruCache<String, DominantColors>(cacheSize)
            else -> null
        }

    suspend fun updateColorsFromImageUri(uri: String) {
        val result = calculateDominantColor(uri)
        color = result?.color ?: defaultColor
    }

    private suspend fun calculateDominantColor(url: String): DominantColors? {
        val cached = cache?.get(url)
        if (cached != null) {
            // If we already have the result cached, return early now...
            return cached
        }

        // Otherwise we calculate the swatches in the image, and return the first valid color
        return calculateSwatchesInCoverImage(context, url)
            // First we want to sort the list by the color's population
            .sortedByDescending { swatch -> swatch.population }
            // Then we want to find the first valid color
            .firstOrNull { swatch -> isColorValid(Color(swatch.rgb)) }
            // If we found a valid swatch, wrap it in a [DominantColors]
            ?.let { swatch ->
                DominantColors(
                    color = Color(swatch.rgb)
                )
            }
            // Cache the resulting [DominantColors]
            ?.also { result -> cache?.put(url, result) }
    }

    /**
     * Reset the color values to [defaultColor].
     */
    fun reset() {
        color = defaultColor
    }
}

@Immutable
private data class DominantColors(val color: Color)

private suspend fun calculateSwatchesInCoverImage(
    context: Context,
    coverUri: String
): List<Palette.Swatch> {
    val request =
        ImageRequest.Builder(context)
            .data(coverUri)
            // We scale the image to cover 128px x 128px (i.e. min dimension == 128px)
            .size(128).scale(Scale.FILL)
            // Disable hardware bitmaps, since Palette uses Bitmap.getPixels()
            .allowHardware(false)
            // Set a custom memory cache key to avoid overwriting the displayed image in the cache
            .memoryCacheKey("$coverUri.palette")
            .build()

    val bitmap =
        when (val result = context.imageLoader.execute(request)) {
            is SuccessResult -> result.drawable.toBitmap()
            else -> null
        }

    return bitmap?.let {
        withContext(Dispatchers.Default) {
            val palette =
                Palette.Builder(bitmap)
                    // Disable any bitmap resizing in Palette. We've already loaded an appropriately
                    // sized bitmap through Coil
                    .resizeBitmapArea(0)
                    // Clear any built-in filters. We want the unfiltered dominant color
                    .clearFilters()
                    // We reduce the maximum color count down to 8
                    .maximumColorCount(8)
                    .generate()

            palette.swatches
        }
    } ?: emptyList()
}

@Composable
fun NowPlayingDynamicTheme(
    coverUri: String,
    content: @Composable () -> Unit
) {
    val surfaceColor = MaterialTheme.colorScheme.surface
    val dominantColorState =
        rememberDominantColorState(
            defaultColor = MaterialTheme.colorScheme.surface
        ) { color ->
            // We want a color which has sufficient contrast against the surface color
            color.contrastAgainst(surfaceColor) >= MinContrastOfPrimaryVsSurface
        }
    DynamicThemePrimaryColorsFromImage(dominantColorState) {
        // Update the dominantColorState with colors coming from the podcast image URL
        LaunchedEffect(coverUri) {
            if (coverUri.isNotEmpty()) {
                dominantColorState.updateColorsFromImageUri(coverUri)
            } else {
                dominantColorState.reset()
            }
        }
        content()
    }
}

const val MinContrastOfPrimaryVsSurface = 3f

fun Color.contrastAgainst(background: Color): Float {
    val fg = if (alpha < 1f) compositeOver(background) else this

    val fgLuminance = fg.luminance() + 0.05f
    val bgLuminance = background.luminance() + 0.05f

    return max(fgLuminance, bgLuminance) / min(fgLuminance, bgLuminance)
}