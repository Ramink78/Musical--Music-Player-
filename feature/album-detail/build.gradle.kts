import musical.build.implementation

plugins {
    alias(libs.plugins.musical.feature)
}

android {
    namespace = "rk.musical.feature.albumDetail"
}

dependencies {
    implementation(projects.domain.albums)
    implementation(libs.coil.compose)
}