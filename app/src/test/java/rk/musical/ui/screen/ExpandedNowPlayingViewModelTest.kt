package rk.musical.ui.screen

import io.mockk.mockk
import io.mockk.verify
import junit.framework.TestCase.assertEquals
import junit.framework.TestCase.assertFalse
import junit.framework.TestCase.assertTrue
import org.junit.Before
import org.junit.Test
import rk.core.player.MusicalRemote
import rk.musical.data.FavoriteRepository
import rk.musical.data.LyricRepository
import rk.musical.data.model.Song

class ExpandedNowPlayingViewModelTest {
    lateinit var viewModel: ExpandedNowPlayingViewModel
    lateinit var lyricRepository: LyricRepository
    lateinit var favoriteRepository: FavoriteRepository
    lateinit var musicalRemote: MusicalRemote

    @Before
    fun setup() {
        lyricRepository = mockk(relaxed = true)
        favoriteRepository = mockk(relaxed = true)
        musicalRemote = mockk(relaxed = true)
        viewModel = ExpandedNowPlayingViewModel(musicalRemote, lyricRepository, favoriteRepository)
    }

    @Test
    fun `initial state should be empty song and not playing`() {
        val expectedState = ExpandedNowPlayingUiState(
            currentSong = Song.Empty,
            isFavorite = false,
            totalTime = "00:00",
            currentTime = "00:00",
            isPlaying = false,
            playbackPosition = 0L
        )
        assertEquals(expectedState, viewModel.nowPlayingUiStateFlow.value)
    }

    @Test
    fun `when state is playing and toggle play call it's should be is not playing`() {
        viewModel.togglePlay()
    }

    @Test
    fun `when show lyric called the isVisibleLyric state should be true`() {
        viewModel.showLyricCover()
        assertTrue(viewModel.lyricUiState.value.isVisibleLyric)
    }

    @Test
    fun `when hide lyric called the isVisibleLyric state should be false`() {
        viewModel.hideLyricCover()
        assertFalse(viewModel.lyricUiState.value.isVisibleLyric)
    }

    @Test
    fun `when seek to a specific position the musical remote should be seek to that position`() {
        val specificPosition = 134L
        viewModel.seekToProgress(specificPosition)
        verify { musicalRemote.seekToPosition(specificPosition) }
    }
}
