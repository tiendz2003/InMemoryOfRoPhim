plugins {
    alias(libs.plugins.rophim.android.feature.api)
}

android {
    namespace = "com.manutd.ronaldo.api"
}
dependencies {
    implementation(projects.core.domain)
}