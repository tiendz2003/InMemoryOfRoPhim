plugins {
    alias(libs.plugins.base.jvm.library)
    alias(libs.plugins.base.android.hilt)
    //alias(libs.plugins.base.android.library.compose)
}

dependencies {
    implementation(libs.androidx.core.ktx)
}