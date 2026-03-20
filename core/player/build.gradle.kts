plugins {
    alias(libs.plugins.base.android.library)
    alias(libs.plugins.base.android.hilt)
    alias(libs.plugins.compose)
}

android {
    namespace = "com.manutd.rophim.core.player"
}

dependencies {
    implementation(libs.androidx.compose.material3)
  //  implementation(libs.media3.ffmpeg)
    api(libs.media3.common)
    api(libs.media3.common.ktx)
    api(libs.androidx.media3.datasource.okhttp)
    api(libs.exoplayer.core)
    api(libs.media3.exoplayer.hls)
    api(libs.exoplayer.dash)
    api(libs.media3.effect)
    api(libs.media3.session)
    api(libs.exoplayer.ui)
    api(libs.android.concurrency)
    /*    compileOnly(libs.checkerframework)
        compileOnly(libs.google.errorProne.annotations)*/
    implementation(libs.exoplayer.compose)

}