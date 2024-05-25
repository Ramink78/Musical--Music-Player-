package rk.ui.songs

import rk.core.SortOrder
import rk.domain.model.Track

sealed interface SongsScreenState {
    data object Loading : SongsScreenState
    data object Empty : SongsScreenState
    data class Loaded(val tracks: List<Track>, val sortOrder: SortOrder) : SongsScreenState
}

data class SongsScreenUiModel(
    val isLoading: Boolean = true,
    val isEmpty: Boolean = false,
    val tracks: List<Track> = emptyList(),
    val sortOrder: SortOrder = SortOrder.DateAddedDesc,
    val currentTrack: Track? = null
)