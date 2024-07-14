import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    `kotlin-dsl`
}

group = "musical.build"

java {
    sourceCompatibility = JavaVersion.VERSION_17
    targetCompatibility = JavaVersion.VERSION_17
}

tasks.withType<KotlinCompile>().configureEach {
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
}

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.android.tools.common)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.ksp.gradlePlugin)
}

tasks {
    validatePlugins {
        enableStricterValidation = true
        failOnWarning = true
    }
}
gradlePlugin {
    val pluginPackage = "$group.plugin"
    plugins {
        register("androidApplication") {
            id = "musical.Application"
            implementationClass = "$pluginPackage.AppModulePlugin"
        }
        register("androidApplicationCompose") {
            id = "musical.ApplicationCompose"
            implementationClass = "$pluginPackage.AndroidApplicationComposePlugin"
        }
        register("androidHilt") {
            id = "musical.Hilt"
            implementationClass = "$pluginPackage.HiltPlugin"
        }
        register("androidLibrary") {
            id = "musical.AndroidLibrary"
            implementationClass = "$pluginPackage.AndroidLibraryPlugin"
        }
        register("androidLibraryCompose") {
            id = "musical.AndroidLibraryCompose"
            implementationClass = "$pluginPackage.AndroidLibraryComposePlugin"
        }
        register("androidFeature") {
            id = "musical.Feature"
            implementationClass = "$pluginPackage.FeatureModulePlugin"
        }
        register("androidRoom") {
            id = "musical.AndroidRoom"
            implementationClass = "$pluginPackage.RoomPlugin"
        }
        register("domain") {
            id = "musical.Domain"
            implementationClass = "$pluginPackage.DomainModulePlugin"
        }
    }
}