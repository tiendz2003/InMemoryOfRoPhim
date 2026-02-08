plugins {
    alias(libs.plugins.base.android.library)
    alias(libs.plugins.base.android.library.compose)
}

android {
    namespace = "com.manutd.ronaldo.core.ui"
}

dependencies {
   /* api(libs.androidx.metrics)
    api(projects.core.analytics)
    api(projects.core.designsystem)*/
    api(projects.core.model)
    api(projects.core.designsystem)
    api(libs.mavericks)
    //implementation(libs.androidx.browser)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.compose)

   /* androidTestImplementation(libs.bundles.androidx.compose.ui.test)
    androidTestImplementation(projects.core.testing)*/
}