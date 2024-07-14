plugins {
    alias(libs.plugins.musical.library)
    alias(libs.plugins.musical.hilt)
}

android {
    namespace = "rk.data.songs"
}

dependencies {
    implementation(projects.data.model)
    implementation(projects.core)
}