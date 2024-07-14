plugins {
    alias(libs.plugins.musical.library)
    alias(libs.plugins.musical.hilt)
}

android {
    namespace = "rk.data.albums"
}

dependencies {
    implementation(projects.core)
    implementation(projects.data.model)
}