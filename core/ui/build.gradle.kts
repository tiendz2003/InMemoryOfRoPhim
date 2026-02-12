plugins {
    alias(libs.plugins.base.android.library)
    alias(libs.plugins.base.android.library.compose)
}

android {
    namespace = "com.manutd.ronaldo.core.ui"
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    api(projects.core.model)
    api(projects.core.designsystem)
    api(libs.mavericks)
    api(libs.coil.kt.network)
    api(libs.coil.kt.compose)
    api(libs.coil)

    /* androidTestImplementation(libs.bundles.androidx.compose.ui.test)
     androidTestImplementation(projects.core.testing)*/
}