package rk.musical.ui.screen

import io.mockk.mockk
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import rk.core.player.MusicalRemote
import rk.ui.nowplaying.expanded.FullNowPlayingScreenViewModel

class FullNowPlayingScreenViewModelTest {
    lateinit var viewModel: FullNowPlayingScreenViewModel
    lateinit var musicalRemote: MusicalRemote

    @Before
    fun setup() {
        musicalRemote = mockk(relaxed = true)
        viewModel = FullNowPlayingScreenViewModel(musicalRemote)
    }

    @Test
    fun `initial state should be empty song and not playing`() {
    }

    @Test
    fun `when state is playing and toggle play call it's should be is not playing`() {
        viewModel.togglePlay()
    }

    @Test
    fun `when show lyric called the isVisibleLyric state should be true`() {
    }

    @Test
    fun `when hide lyric called the isVisibleLyric state should be false`() {
    }

    @Test
    fun `when seek to a specific position the musical remote should be seek to that position`() {
        val specificPosition = 134L
        viewModel.seekToProgress(specificPosition)
        verify { musicalRemote.seekToPosition(specificPosition) }
    }
}
