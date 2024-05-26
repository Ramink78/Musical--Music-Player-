package rk.musical.ui.screen

import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import rk.core.player.MusicalRemote
import rk.musical.data.model.Song
import rk.ui.nowplaying.collapsed.CollapsedNowPlayingUiState
import rk.ui.nowplaying.collapsed.MiniNowPlayingViewModel

class MiniNowPlayingViewModelTest {

    lateinit var viewModel: MiniNowPlayingViewModel
    lateinit var mockedMusicalRemote: MusicalRemote

    @Before
    fun setup() {
        mockedMusicalRemote = mockk(relaxed = true)
        viewModel = MiniNowPlayingViewModel(mockedMusicalRemote)
    }

    @Test
    fun `initial state should be is not playing and empty song`() {
        val expectedState =
            CollapsedNowPlayingUiState(
                isPlaying = false,
                playingSong = Song.Empty
            )
        assertEquals(expectedState, viewModel.uiState.value)
    }
}
