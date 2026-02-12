plugins {
    alias(libs.plugins.base.android.library)
    alias(libs.plugins.base.android.library.compose)
}

android {
    namespace = "com.manutd.rophim.core.designsystem"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {

    api(libs.androidx.compose.foundation)
    api(libs.androidx.compose.foundation.layout)
    api(libs.androidx.compose.material.iconsExtended)
    api(libs.androidx.compose.material3)
    api(libs.androidx.compose.material3.adaptive)
    api(libs.androidx.compose.material3.navigationSuite)
    api(libs.androidx.compose.runtime)
    api(libs.androidx.compose.ui.util)
    api(libs.dotsindicator)
    api(libs.androidx.lifecycle.runtimeCompose)
    api(libs.androidx.lifecycle.viewModelCompose)
    api(libs.compose.cloudy)
    implementation(libs.coil.kt.network)
    implementation(libs.coil.kt.compose)


    /*testImplementation(libs.androidx.compose.ui.test)
    testImplementation(libs.androidx.compose.ui.testManifest)

    testImplementation(libs.hilt.android.testing)
    testImplementation(libs.robolectric)
    testImplementation(projects.core.screenshotTesting)*/
}