import com.android.build.api.dsl.LibraryExtension
import com.manutd.rophim.build_logic.convention.libs
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.kotlin.dsl.apply
import org.gradle.kotlin.dsl.configure
import org.gradle.kotlin.dsl.dependencies

class AndroidFeatureImplConventionPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        with(target){
            apply(plugin = "base.android.library")
            apply(plugin = "base.android.hilt")
            extensions.configure<LibraryExtension> {
                testOptions.animationsDisabled = true
               // configureGradleManagedDevices(this)
            }
            dependencies {
                "implementation"(project(":core:ui"))
                "implementation"(project(":core:designsystem"))

                "implementation"(libs.findLibrary("androidx.lifecycle.runtimeCompose").get())
                "implementation"(libs.findLibrary("androidx.lifecycle.viewModelCompose").get())
                "implementation"(libs.findLibrary("androidx.hilt.lifecycle.viewModelCompose").get())
                "implementation"(libs.findLibrary("androidx.navigation3.runtime").get())
                "implementation"(libs.findLibrary("androidx.tracing.ktx").get())
                "implementation"(libs.findLibrary("mavericks").get())
                "implementation"(libs.findLibrary("mavericks.hilt").get())
                "implementation"(libs.findLibrary("mavericks.compose").get())
                "implementation"(libs.findLibrary("mavericks.test").get())

                "androidTestImplementation"(
                    libs.findLibrary("androidx.lifecycle.runtimeTesting").get(),
                )
            }
        }
    }
}