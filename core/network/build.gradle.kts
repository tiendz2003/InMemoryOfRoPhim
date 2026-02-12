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
    defaultConfig {
        consumerProguardFiles("consumer-rules.pro")
    }
}

dependencies {
    implementation(libs.kotlinx.collections.immutable)
    implementation(libs.androidx.tracing.ktx)
    api(libs.bundles.sandwich.networking)
    implementation(libs.coil.kt.network)
    api(projects.core.common)
    api(projects.core.model)
    implementation(libs.okhttp.logging)
    implementation(libs.retrofit.core)
    implementation(libs.retrofit.kotlin.serialization)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.bundles.sandwich.networking)
}