import musical.build.implementation

plugins {
    alias(libs.plugins.musical.feature)
}

android {
    namespace = "rk.musical.albums"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(projects.data.model)
    implementation(projects.domain.albums)
}