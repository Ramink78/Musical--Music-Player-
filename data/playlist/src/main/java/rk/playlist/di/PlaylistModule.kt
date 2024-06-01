package rk.playlist.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import rk.data.SongRepository
import rk.playlist.PlaylistRepository
import rk.playlist.PlaylistRepositoryImpl

@Module
@InstallIn(SingletonComponent::class)
object PlaylistModule {

    @Provides
    fun providePlaylistRepo(
        @ApplicationContext context: Context,
        songRepository: SongRepository
    ): PlaylistRepository {
        return PlaylistRepositoryImpl(
            contentResolver = context.contentResolver,
            songRepository = songRepository
        )
    }
}