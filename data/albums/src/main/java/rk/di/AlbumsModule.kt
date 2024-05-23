package rk.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import rk.data.AlbumRepository
import rk.data.LocalAlbumRepository
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AlbumsModule {

    @Singleton
    @Provides
    fun provideAlbumRepo(
        @ApplicationContext context: Context
    ): AlbumRepository =
        LocalAlbumRepository(context)
}