package rk.musical.ui.screen

import android.content.Intent
import android.net.Uri
import android.provider.Settings
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.ArrowDownward
import androidx.compose.material.icons.rounded.ArrowUpward
import androidx.compose.material.icons.rounded.MusicNote
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import rk.core.SortOrder
import rk.musical.R
import rk.musical.ui.RationaleWarning
import rk.musical.ui.RequiredMediaPermission
import rk.musical.ui.mediaPermission
import rk.musical.ui.theme.MusicalTheme

@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun SongsScreen(
    modifier: Modifier = Modifier,
    contentPadding: PaddingValues = PaddingValues()
) {
    val context = LocalContext.current
    val permissionState = rememberPermissionState(permission = mediaPermission)

    RequiredMediaPermission(
        permissionState = permissionState,
        grantedContent = {
            rk.ui.songs.SongsScreen(modifier = modifier)
        },
        rationalContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RationaleWarning(
                    onRequest = { permissionState.launchPermissionRequest() },
                    buttonText = "Request",
                    rationaleText = stringResource(R.string.songs_permission_rationale),
                    icon = Icons.Rounded.MusicNote,
                    rationaleTitle = stringResource(R.string.media_permission_title)
                )
            }
        },
        deniedContent = {
            Column(
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                RationaleWarning(
                    onRequest = {
                        context.startActivity(
                            Intent(
                                Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                Uri.fromParts("package", context.packageName, null)
                            )
                        )
                    },
                    buttonText = "Grant in setting",
                    icon = Icons.Rounded.MusicNote,
                    rationaleText = stringResource(R.string.songs_permission_rationale),
                    rationaleTitle = stringResource(R.string.media_permission_title)
                )
            }
        }
    )
}

@Composable
fun OrderSelector(
    modifier: Modifier = Modifier,
    initialOrder: SortOrder,
    onChanged: (SortOrder) -> Unit
) {
    val isDateAddedSelected = initialOrder == SortOrder.DateAddedDesc ||
        initialOrder == SortOrder.DateAddedAsc
    val isNameSelected = initialOrder == SortOrder.NameAsc ||
        initialOrder == SortOrder.NameDesc
    var shouldShowDescIcon by remember {
        mutableStateOf(
            initialOrder == SortOrder.DateAddedDesc ||
                initialOrder == SortOrder.NameDesc
        )
    }
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterHorizontally)
    ) {
        FilterChip(
            shape = CircleShape,
            selected = isDateAddedSelected,
            onClick = {
                if (initialOrder == SortOrder.DateAddedDesc) {
                    onChanged(SortOrder.DateAddedAsc)
                    shouldShowDescIcon = false
                } else {
                    onChanged(SortOrder.DateAddedDesc)
                    shouldShowDescIcon = true
                }
            },
            label = {
                Text(
                    text = stringResource(R.string.sort_selector_date_added),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingIcon = {
                if (isDateAddedSelected) {
                    if (shouldShowDescIcon) {
                        Icon(imageVector = Icons.Rounded.ArrowDownward, contentDescription = "")
                    } else {
                        Icon(imageVector = Icons.Rounded.ArrowUpward, contentDescription = "")
                    }
                }
            }
        )
        FilterChip(
            shape = CircleShape,
            selected = isNameSelected,
            onClick = {
                if (initialOrder == SortOrder.NameDesc) {
                    onChanged(SortOrder.NameAsc)
                    shouldShowDescIcon = false
                } else {
                    onChanged(SortOrder.NameDesc)
                    shouldShowDescIcon = true
                }
            },
            label = {
                Text(
                    text = stringResource(R.string.sort_selector_name),
                    style = MaterialTheme.typography.bodyMedium
                )
            },
            trailingIcon = {
                if (isNameSelected) {
                    if (shouldShowDescIcon) {
                        Icon(imageVector = Icons.Rounded.ArrowDownward, contentDescription = "")
                    } else {
                        Icon(imageVector = Icons.Rounded.ArrowUpward, contentDescription = "")
                    }
                }
            }
        )
    }
}

@Preview
@Composable
private fun OrderSelectorPreview() {
    MusicalTheme(darkTheme = true) {
        OrderSelector(
            modifier = Modifier.fillMaxWidth(),
            initialOrder = SortOrder.DateAddedAsc
        ) {
        }
    }
}
