package musical

import com.android.build.api.dsl.CommonExtension
import musical.build.androidTestImplementation
import musical.build.debugImplementation
import musical.build.implementation
import musical.build.libs
import org.gradle.api.Project
import org.gradle.kotlin.dsl.dependencies

internal fun Project.configureAndroidCompose(
    commonExtension: CommonExtension<*, *, *, *, *, *>,
) {
    commonExtension.apply {
        buildFeatures {
            compose = true
        }



        dependencies {
            val bom = libs.findLibrary("compose-bom").get()
            implementation(platform(bom))
            androidTestImplementation(platform(bom))
            implementation(libs.findLibrary("compose-material3").get())
            debugImplementation(libs.findLibrary("compose-ui-tooling").get())
            implementation(libs.findLibrary("compose-ui-tooling-preview").get())
            implementation(libs.findLibrary("compose-runtime").get())
        }
    }
}