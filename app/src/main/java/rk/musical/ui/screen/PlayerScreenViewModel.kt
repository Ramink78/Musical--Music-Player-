package rk.musical.ui.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import rk.core.player.MusicalRemote

@HiltViewModel
class PlayerScreenViewModel
@Inject
constructor
(private val musicalRemote: MusicalRemote) : ViewModel() {
    val isVisibleSheetFlow =
        musicalRemote.playingMediaItemFlow
            .distinctUntilChanged()
            .map {
                it != null
                //   it != Song.Empty
            }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(5000),
                initialValue = false
            )
}
