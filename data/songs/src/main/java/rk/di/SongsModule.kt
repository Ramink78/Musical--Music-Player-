package rk.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import rk.data.LocalSongRepository
import rk.data.SongRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
internal class SongsModule {
    @Singleton
    @Provides
    fun provideSongRepository(@ApplicationContext context: Context): SongRepository {
        return LocalSongRepository(context)
    }
}