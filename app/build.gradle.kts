import org.gradle.kotlin.dsl.implementation

plugins {
    alias(libs.plugins.base.android.application)
    alias(libs.plugins.base.android.application.compose)
    alias(libs.plugins.base.android.hilt)
}

android {
    namespace = "com.manutd.rophim"

    defaultConfig {
        applicationId = "com.manutd.rophim"
        versionCode = 1
        versionName = "0.0.1"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        release {
            isMinifyEnabled = true
            isShrinkResources = true
        }
    }

}

dependencies {
    implementation(projects.feature.login)
    implementation(projects.feature.home)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.activity.compose)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    implementation(libs.androidx.navigation.compose)
}
