plugins {
    alias(libs.plugins.base.android.library)
    alias(libs.plugins.base.android.hilt)
    id("kotlinx-serialization")
}

android {
    buildFeatures {
        buildConfig = true
    }
    namespace = "com.manutd.rophim.core.network"
    testOptions.unitTests.isIncludeAndroidResources = true
}

dependencies {
    implementation(libs.androidx.tracing.ktx)
    implementation(libs.coil.kt)
    implementation(libs.coil.kt.svg)
    api(libs.bundles.sandwich.networking)
    api(projects.core.common)
    api(projects.core.model)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.sandwich.networking)
}