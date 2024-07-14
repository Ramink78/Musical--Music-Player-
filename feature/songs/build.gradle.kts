plugins {
    alias(libs.plugins.musical.feature)
}

android {
    namespace = "rk.ui.songs"
}

dependencies {
    implementation(projects.domain.songs)
    implementation(libs.coil.compose)
    implementation(libs.kotlinx.collections.immutable)
}
