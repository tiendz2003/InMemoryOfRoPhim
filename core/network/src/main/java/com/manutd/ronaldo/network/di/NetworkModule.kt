package com.manutd.ronaldo.network.di

import android.content.Context
import com.manutd.rophim.core.network.BuildConfig
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import kotlinx.serialization.json.Json
import okhttp3.Call
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import javax.inject.Singleton
import androidx.tracing.trace
import coil3.ImageLoader
import coil3.disk.DiskCache
import coil3.disk.directory
import coil3.memory.MemoryCache
import coil3.network.okhttp.OkHttpNetworkFetcherFactory
import coil3.request.crossfade
import coil3.util.DebugLogger
import com.manutd.ronaldo.network.retrofit.RetrofitRoNetworkApi
import com.skydoves.sandwich.retrofit.adapters.ApiResponseCallAdapterFactory
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.converter.kotlinx.serialization.asConverterFactory
import java.nio.file.attribute.AclEntry.newBuilder
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
internal object NetworkModule {
    @Provides
    @Singleton
    fun provideJson(): Json {
        return Json {
            isLenient = true
            encodeDefaults = true
            ignoreUnknownKeys = true
            explicitNulls = false
        }
    }

    @Provides
    @Singleton
    fun okHttpCallFactory(): Call.Factory = trace("RoOkHttpClient") {
        OkHttpClient.Builder()
            .addInterceptor(
                HttpLoggingInterceptor()
                    .apply {
                        if (BuildConfig.DEBUG) {
                            setLevel(HttpLoggingInterceptor.Level.BODY)
                        }
                    },
            )
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(
        okHttpCallFactory: dagger.Lazy<Call.Factory>,
        json: Json
    ): Retrofit {
        val contentType = "application/json".toMediaType()
        return Retrofit.Builder()
            .baseUrl("https://iptv.rophim.dev/")
            .callFactory {
                okHttpCallFactory.get().newCall(it)
            }
            .addConverterFactory(json.asConverterFactory(contentType))
            .addCallAdapterFactory(ApiResponseCallAdapterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRoNetwork(retrofit: Retrofit): RetrofitRoNetworkApi {
        return retrofit.create(RetrofitRoNetworkApi::class.java)
    }

    /**
     * Since we're displaying SVGs in the app, Coil needs an ImageLoader which supports this
     * format. During Coil's initialization it will call `applicationContext.newImageLoader()` to
     * obtain an ImageLoader.
     *
     * @see <a href="https://github.com/coil-kt/coil/blob/main/coil-singleton/src/main/java/coil/Coil.kt">Coil</a>
     */
    @Provides
    @Singleton
    fun imageLoader(
        okHttpCallFactory: dagger.Lazy<Call.Factory>,
        @ApplicationContext context: Context,
    ): ImageLoader = trace("RoImageLoader") {
        ImageLoader.Builder(context)
            .crossfade(true)
            .components {
                add(
                    OkHttpNetworkFetcherFactory(
                        callFactory = {
                            okHttpCallFactory.get().apply {
                                newBuilder().build()
                            } as OkHttpClient

                        }
                    )
                )
            }
            .memoryCache {
                MemoryCache.Builder()
                    .maxSizePercent(context, 0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(context.cacheDir.resolve("image_cache"))
                    .maxSizePercent(0.02)
                    .build()
            }
            .build()
    }
}