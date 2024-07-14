plugins {
    alias(libs.plugins.musical.feature)
}

android {
    namespace = "rk.playlist"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(projects.domain.playlist)
}