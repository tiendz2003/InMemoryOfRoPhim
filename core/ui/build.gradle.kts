import org.gradle.kotlin.dsl.libs

plugins {
    alias(libs.plugins.base.android.hilt)
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
    api(libs.bundles.exoplayer)
    api(projects.core.model)
    api(projects.core.designsystem)
    api(libs.bundles.maverick)
    api(libs.coil.kt.network)
    api(libs.coil.kt.compose)
    api(libs.coil)
    implementation(libs.androidx.media3.datasource.okhttp)


    /* androidTestImplementation(libs.bundles.androidx.compose.ui.test)
     androidTestImplementation(projects.core.testing)*/
}