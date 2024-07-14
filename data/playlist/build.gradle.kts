plugins {
    alias(libs.plugins.musical.library)
    alias(libs.plugins.musical.hilt)
}

android {
    namespace = "rk.playlist"
}

dependencies {
    implementation(projects.core)
    implementation(projects.data.songs)
    implementation(projects.data.model)
}