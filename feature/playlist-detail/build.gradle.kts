plugins {
    alias(libs.plugins.musical.feature)
}

android {
    namespace = "rk.musical.playlist.detail"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(projects.domain.playlist)
}