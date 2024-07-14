package rk.core.component

import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun Loading(
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colorScheme.primary,
    strokeWidth: Dp = 2.dp,
    strokeCap: StrokeCap = StrokeCap.Round,
    trackColor: Color = MaterialTheme.colorScheme.onBackground
) {
    CircularProgressIndicator(
        modifier = modifier,
        color = color,
        strokeCap = strokeCap,
        strokeWidth = strokeWidth,
        trackColor = trackColor
    )

}

@Preview
@Composable
private fun LoadingPreview() {
    Loading()
}