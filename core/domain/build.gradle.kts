plugins {
    alias(libs.plugins.base.android.library)
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.manutd.ronaldo.domain"
}

dependencies {
    api(projects.core.data)
    api(projects.core.model)

    implementation(libs.javax.inject)

   // testImplementation(projects.core.testing)
}