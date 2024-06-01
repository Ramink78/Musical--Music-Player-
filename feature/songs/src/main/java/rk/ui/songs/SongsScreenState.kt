package rk.ui.songs

import rk.core.SortOrder
import rk.musical.domain.model.Track


data class SongsScreenUiModel(
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
    val tracks: List<Track> = emptyList(),
    val sortOrder: SortOrder = SortOrder.DateAddedDesc,
    val currentTrack: Track? = null
)