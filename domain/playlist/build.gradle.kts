plugins {
    alias(libs.plugins.musical.domain)
}

android {
    namespace = "rk.playlist"
}

dependencies {
    implementation(projects.data.playlist)
    implementation(projects.data.songs)
}