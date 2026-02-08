plugins {
    alias(libs.plugins.rophim.android.feature.impl)
    alias(libs.plugins.base.android.library.compose)
}

android {
    namespace = "com.manutd.ronaldo.impl"
    testOptions.unitTests.isIncludeAndroidResources = true

}

dependencies {
    testImplementation("io.mockk:mockk:1.14.9")

    // 4. Coroutines Test (Để dùng runTest)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.10.2")
    implementation(projects.core.domain)
    implementation(projects.feature.home.api)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    testImplementation(libs.hilt.android.testing)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.bundles.androidx.compose.ui.test)
}