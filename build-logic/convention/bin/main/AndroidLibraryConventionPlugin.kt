import com.manutd.rophim.build_logic.convention.configureKotlinAndroid
import com.manutd.rophim.build_logic.convention.testImplementation
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies
import org.gradle.kotlin.dsl.kotlin
import com.android.build.api.dsl.LibraryExtension

class AndroidLibraryConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target) {
            apply(plugin = "com.android.library")
         //   apply(plugin = "org.jetbrains.kotlin.android")


            extensions.configure<LibraryExtension>("android") {
                configureKotlinAndroid(this)
                testOptions.targetSdk = 36
                defaultConfig.testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
                testOptions.animationsDisabled = true
                resourcePrefix =
                    path.split("""\W""".toRegex()).drop(1).distinct().joinToString(separator = "_")
                        .lowercase() + "_"
            }

            dependencies {
                testImplementation(kotlin("test"))
            }
        }
    }
}