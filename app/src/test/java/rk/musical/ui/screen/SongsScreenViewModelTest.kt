package rk.musical.ui.screen

import io.mockk.coEvery
import io.mockk.mockk
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.test.runTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import rk.domain.SongsUseCase
import rk.musical.data.model.Song
import rk.musical.player.MusicalRemote

class SongsScreenViewModelTest {
    lateinit var viewModel: SongsScreenViewModel
    lateinit var mockedSongsRepository: SongRepository
    lateinit var mockedMusicalRemote: MusicalRemote
    lateinit var mockedSongsUseCase: SongsUseCase

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    @Before
    fun setup() {
        mockedSongsRepository = mockk()
        mockedMusicalRemote = mockk(relaxed = true)
        viewModel =
            SongsScreenViewModel(mockedSongsRepository, mockedSongsUseCase, mockedMusicalRemote)
    }

    @Test
    fun `when a fresh viewmodel created default state should be loading`() {
        val state = viewModel.uiState.value
        assertEquals(state, SongsScreenUiState.Loading)
    }

    @Test
    fun `when there isn't any song the state should be empty`() = runTest {
        coEvery { mockedSongsRepository.loadSongs() } returns emptyList()
        viewModel.loadSongs()
        assertEquals(SongsScreenUiState.Empty, viewModel.uiState.value)
    }

    @Test
    fun `when songs loaded the state should be Loaded with list of Songs`() = runTest {
        val listOfSong = listOf(
            Song(
                id = "1",
                artist = "artist1",
                albumName = "alName1",
                albumId = "1",
                duration = 1,
                songUri = "uri1",
                title = "title1"
            ),
            Song(
                id = "2",
                artist = "artist2",
                albumName = "alName2",
                albumId = "2",
                duration = 2,
                songUri = "uri2",
                title = "title2"
            )

        )
        coEvery { mockedSongsRepository.loadSongs() } returns listOfSong
        viewModel.loadSongs()
        assertEquals(SongsScreenUiState.Loaded(listOfSong), viewModel.uiState.value)
    }
}
