plugins {
    alias(libs.plugins.base.android.library)
    alias(libs.plugins.base.android.hilt)
}

android {
    namespace = "com.manutd.rophim.core.data"
}

dependencies {
    api(projects.core.common)
    /*api(projects.core.database)
    api(projects.core.datastore)*/
    api(projects.core.network)
}