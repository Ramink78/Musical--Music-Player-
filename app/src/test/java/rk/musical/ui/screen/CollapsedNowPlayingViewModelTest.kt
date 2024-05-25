package rk.musical.ui.screen

import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import org.junit.Before
import org.junit.Test
import rk.core.player.MusicalRemote
import rk.musical.data.model.Song

class CollapsedNowPlayingViewModelTest {

    lateinit var viewModel: CollapsedNowPlayingViewModel
    lateinit var mockedMusicalRemote: MusicalRemote

    @Before
    fun setup() {
        mockedMusicalRemote = mockk(relaxed = true)
        viewModel = CollapsedNowPlayingViewModel(mockedMusicalRemote)
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
