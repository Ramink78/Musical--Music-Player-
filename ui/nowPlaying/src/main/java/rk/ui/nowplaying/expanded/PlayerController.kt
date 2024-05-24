package rk.ui.nowplaying.expanded

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Pause
import androidx.compose.material.icons.rounded.PlayArrow
import androidx.compose.material.icons.rounded.Repeat
import androidx.compose.material.icons.rounded.RepeatOne
import androidx.compose.material.icons.rounded.Shuffle
import androidx.compose.material.icons.rounded.SkipNext
import androidx.compose.material.icons.rounded.SkipPrevious
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import rk.core.PlayerState
import rk.core.RepeatMode
import rk.ui.common.Padding

@Composable
internal fun PlayerController(
    modifier: Modifier = Modifier,
    playerState: PlayerState,
    onSkipNext: () -> Unit,
    onSkipPrevious: () -> Unit,
    onTogglePlay: () -> Unit,
    onShuffleClick: () -> Unit,
    onRepeatModeClick: () -> Unit,
    onPositionChanged: (Long) -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = playerState.passedTime,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(start = Padding.m)

            )
            Text(
                text = playerState.totalTime,
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier
                    .padding(
                        end = Padding.m
                    )
            )
        }


        Slider(
            modifier =
            Modifier
                .fillMaxWidth()
                .padding(horizontal = Padding.m),
            value = 0f,
            onValueChange = {}
        )
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = Padding.s, bottom = Padding.s)
        ) {
            val playImageVector =
                if (playerState.isPlaying) {
                    Icons.Rounded.Pause
                } else {
                    Icons.Rounded.PlayArrow
                }
            RepeatModeButton(
                enableColor = MaterialTheme.colorScheme.background,
                disableColor = Color.Transparent,
                icon = when (playerState.repeatMode) {
                    RepeatMode.All -> Icons.Rounded.Repeat
                    RepeatMode.One -> Icons.Rounded.RepeatOne
                    RepeatMode.Off -> Icons.Rounded.Repeat
                },
                repeatMode = playerState.repeatMode,
                onRepeatClick = onRepeatModeClick
            )
            IconButton(
                onClick = onSkipPrevious
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipPrevious,
                    contentDescription = ""
                )
            }
            FloatingActionButton(
                onClick = onTogglePlay,
                containerColor = MaterialTheme.colorScheme.background
            ) {
                Icon(
                    imageVector = playImageVector,
                    contentDescription = ""
                )
            }
            IconButton(
                onClick = onSkipNext
            ) {
                Icon(
                    imageVector = Icons.Rounded.SkipNext,
                    contentDescription = ""
                )
            }
            ShuffleModeButton(
                icon = Icons.Rounded.Shuffle,
                enableColor = MaterialTheme.colorScheme.background,
                disableColor = Color.Transparent,
                onShuffleClick = onShuffleClick,
                isEnable = playerState.shuffleMode
            )
        }
    }


}

@Composable
internal fun ShuffleModeButton(
    icon: ImageVector,
    enableColor: Color,
    disableColor: Color,
    onShuffleClick: () -> Unit,
    modifier: Modifier = Modifier,
    isEnable: Boolean = false,
    enableShape: Shape = RoundedCornerShape(8.dp)
) {
    val backgroundColor by animateColorAsState(
        targetValue =
        if (isEnable) {
            enableColor
        } else {
            disableColor
        },
        label = ""
    )
    Box(
        modifier =
        modifier
            .wrapContentSize()
            .padding(Padding.s)
            .clip(enableShape)
            .drawBehind {
                drawRect(backgroundColor)
            }
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onShuffleClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
internal fun RepeatModeButton(
    enableColor: Color,
    disableColor: Color,
    icon: ImageVector,
    onRepeatClick: () -> Unit,
    modifier: Modifier = Modifier,
    enableShape: Shape = RoundedCornerShape(8.dp),
    repeatMode: RepeatMode = RepeatMode.Off
) {
    val backgroundColor by animateColorAsState(
        targetValue =
        if (repeatMode == RepeatMode.Off) {
            disableColor
        } else {
            enableColor
        },
        label = ""
    )
    Box(
        modifier =
        modifier
            .wrapContentSize()
            .padding(Padding.s)
            .clip(enableShape)
            .drawBehind {
                drawRect(backgroundColor)
            }
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                onRepeatClick()
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = icon,
            contentDescription = "",
            tint = MaterialTheme.colorScheme.onSurface
        )
    }
}