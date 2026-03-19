package com.manutd.rophim.di

import com.manutd.rophim.ExoPlayerFactory
import android.content.Context
import androidx.annotation.OptIn
import androidx.media3.common.util.UnstableApi
import androidx.media3.database.StandaloneDatabaseProvider
import androidx.media3.datasource.cache.LeastRecentlyUsedCacheEvictor
import androidx.media3.datasource.cache.SimpleCache
import androidx.media3.datasource.okhttp.OkHttpDataSource
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import java.io.File
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
@OptIn(UnstableApi::class)
object PlayerModule {

    @Provides
    @Singleton
    fun providesSimpleCache(@ApplicationContext context: Context): SimpleCache {
        val cacheDir = File(context.cacheDir, "media_cache")
        val cacheEvictor = LeastRecentlyUsedCacheEvictor(
            256L * 1024 * 1024  // 256 MB
        )
        return SimpleCache(cacheDir, cacheEvictor, StandaloneDatabaseProvider(context))

    }

    @Provides
    @Singleton
    fun provideHttpDataSourceFactory(): OkHttpDataSource.Factory {
        val okHttpClient = OkHttpClient.Builder()
            .connectTimeout(15, TimeUnit.SECONDS)
            .readTimeout(15, TimeUnit.SECONDS)
            .build()
        return OkHttpDataSource.Factory(okHttpClient)
            .setUserAgent("MyApp/1.0 (Android)")
    }

    @Provides
    @Singleton
    fun providePlayerManager(
        @ApplicationContext context: Context,
        cache: SimpleCache,
        httpFactory: OkHttpDataSource.Factory
    ): ExoPlayerFactory = ExoPlayerFactory(context, cache, httpFactory)
}