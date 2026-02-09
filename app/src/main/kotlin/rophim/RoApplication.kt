package rophim

import android.app.Application
import android.content.pm.ApplicationInfo
import android.os.StrictMode
import android.os.StrictMode.ThreadPolicy.Builder
import coil3.ImageLoader
import coil3.PlatformContext
import coil3.SingletonImageLoader
import dagger.hilt.android.HiltAndroidApp
import javax.inject.Inject

@HiltAndroidApp
class RoApplication : Application(), SingletonImageLoader.Factory {
    @Inject
    lateinit var imageLoader: dagger.Lazy<ImageLoader>
    override fun onCreate() {
        super.onCreate()

        setStrictModePolicy()

        // Initialize Sync; the system responsible for keeping data in the app up to date.
        // Sync.initialize(context = this)
    }
    private fun isDebuggable(): Boolean {
        return 0 != applicationInfo.flags and ApplicationInfo.FLAG_DEBUGGABLE
    }

    private fun setStrictModePolicy() {
        if (isDebuggable()) {
            StrictMode.setThreadPolicy(
                Builder().detectAll().penaltyLog().build(),
            )
        }
    }

    override fun newImageLoader(context: PlatformContext): ImageLoader {
        return imageLoader.get()
    }
}