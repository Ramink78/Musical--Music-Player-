plugins {
    alias(libs.plugins.musical.domain)
}

android {
    namespace = "rk.domain.songs"
}

dependencies {
    implementation(projects.data.songs)
}