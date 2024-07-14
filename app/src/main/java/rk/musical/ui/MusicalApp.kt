package rk.musical.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import rk.musical.R
import rk.musical.albums.AlbumsScreen
import rk.musical.feature.albumDetail.AlbumDetailScreen
import rk.musical.navigation.MusicalRoutes
import rk.musical.playlist.detail.PlaylistDetailScreen
import rk.playlist.PlaylistScreen
import rk.ui.nowplaying.collapsed.MiniNowPlaying
import rk.ui.nowplaying.expanded.FullNowPlayingScreen
import rk.ui.songs.SongsScreen

@Composable
fun MusicalApp() {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = backStackEntry?.destination?.route ?: MusicalRoutes.Songs.name

    val navigateToAlbumsScreen =
        remember {
            {
                navController.navigate(MusicalRoutes.Albums.name) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    val navigateToSongsScreen =
        remember {
            {
                navController.navigate(MusicalRoutes.Songs.name) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    val navigateToPlaylistScreen =
        remember {
            {
                navController.navigate(MusicalRoutes.Playlist.name) {
                    popUpTo(navController.graph.findStartDestination().id) {
                        saveState = true
                    }
                    launchSingleTop = true
                    restoreState = true
                }
            }
        }
    Scaffold(
        bottomBar = {
            MusicalBottomBar(
                currentRoute = currentRoute,
                onSelectedAlbums = navigateToAlbumsScreen,
                onSelectedSongs = navigateToSongsScreen,
                isVisible = currentRoute != MusicalRoutes.FullNowPlaying.name,
                onMiniPlayerClick = {
                    navController.navigate(MusicalRoutes.FullNowPlaying.name)
                },
                onPlaylistSelected = navigateToPlaylistScreen
            )
        },
        containerColor = Color.Transparent
    ) { innerPadding ->
        NavHost(
            modifier = Modifier,
            navController = navController,
            startDestination = MusicalRoutes.Songs.name
        ) {
            composable(route = MusicalRoutes.Songs.name) {
                SongsScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPaddingValues = innerPadding

                )
            }
            composable(route = MusicalRoutes.FullNowPlaying.name) {
                FullNowPlayingScreen(modifier = Modifier.fillMaxSize())
            }
            composable(route = MusicalRoutes.Albums.name) {
                AlbumsScreen(
                    modifier = Modifier
                        .fillMaxSize(),
                    contentPadding = innerPadding,
                    onAlbumClicked = { albumId ->
                        navController.navigate(
                            "${MusicalRoutes.AlbumDetail.name}/$albumId"
                        )
                    }
                )
            }
            composable(
                route = "${MusicalRoutes.AlbumDetail.name}/{albumId}",
                arguments =
                listOf(
                    navArgument("albumId") { type = NavType.StringType }
                )
            ) {
                AlbumDetailScreen(
                    albumId = it.arguments?.getString("albumId") ?: ""
                )
            }
            composable(route = MusicalRoutes.Playlist.name) {
                PlaylistScreen(modifier = Modifier.fillMaxSize()) { playlistId ->
                    navController.navigate(
                        "${MusicalRoutes.PlaylistDetail.name}/$playlistId"
                    )
                }
            }
            composable(
                route = "${MusicalRoutes.PlaylistDetail.name}/{playlistId}",
                listOf(
                    navArgument("playlistId") { type = NavType.LongType }
                )
            ) {
                PlaylistDetailScreen(playlistId = it.arguments?.getLong("playlistId") ?: 0)
            }
        }
    }
}

@Composable
fun MusicalBottomBar(
    modifier: Modifier = Modifier,
    currentRoute: String,
    onSelectedAlbums: () -> Unit,
    onSelectedSongs: () -> Unit,
    isVisible: Boolean = true,
    onMiniPlayerClick: () -> Unit,
    onPlaylistSelected: () -> Unit
) {
    AnimatedVisibility(
        visible = isVisible,
        enter = fadeIn() + slideInVertically { it },
        exit = fadeOut() + slideOutVertically { it }
    ) {
        Column(modifier = modifier) {
            MiniNowPlaying(onNavigateToFullNowPlaying = onMiniPlayerClick)
            NavigationBar(
                modifier = Modifier.fillMaxWidth(),
                tonalElevation = 0.dp
            ) {
                NavigationBarItem(
                    selected = currentRoute == MusicalRoutes.Songs.name,
                    onClick = onSelectedSongs,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.music_note_24),
                            contentDescription = stringResource(R.string.songs_tab_cd)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        selectedIconColor = MaterialTheme.colorScheme.surface,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    label = {
                        Text(
                            text = stringResource(R.string.songs),
                            style =
                            if (currentRoute == MusicalRoutes.Songs.name) {
                                MaterialTheme.typography.headlineMedium.copy(fontSize = 14.sp)
                            } else {
                                MaterialTheme.typography.bodyMedium
                            }
                        )
                    }
                )
                NavigationBarItem(
                    selected = currentRoute == MusicalRoutes.Albums.name,
                    onClick = onSelectedAlbums,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.album_24),
                            contentDescription = stringResource(R.string.albums_tab_cd)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        selectedIconColor = MaterialTheme.colorScheme.surface,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    label = {
                        Text(
                            text = stringResource(R.string.albums),
                            style =
                            if (currentRoute == MusicalRoutes.Albums.name) {
                                MaterialTheme.typography.headlineMedium.copy(fontSize = 14.sp)
                            } else {
                                MaterialTheme.typography.bodyMedium
                            }
                        )
                    }
                )

                NavigationBarItem(
                    selected = currentRoute == MusicalRoutes.Playlist.name,
                    onClick = onPlaylistSelected,
                    icon = {
                        Icon(
                            painter = painterResource(id = R.drawable.queue_music_24),
                            contentDescription = stringResource(R.string.playlist)
                        )
                    },
                    colors = NavigationBarItemDefaults.colors(
                        selectedTextColor = MaterialTheme.colorScheme.primary,
                        selectedIconColor = MaterialTheme.colorScheme.surface,
                        indicatorColor = MaterialTheme.colorScheme.primary
                    ),
                    label = {
                        Text(
                            text = stringResource(R.string.playlist),
                            style =
                            if (currentRoute == MusicalRoutes.Playlist.name) {
                                MaterialTheme.typography.headlineMedium.copy(fontSize = 14.sp)
                            } else {
                                MaterialTheme.typography.bodyMedium
                            }
                        )
                    }
                )
            }
        }
    }
}
