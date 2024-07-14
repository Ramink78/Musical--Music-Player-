plugins {
    alias(libs.plugins.musical.domain)
}

android {
    namespace = "rk.domain"
}

dependencies {
    implementation(projects.data.albums)
}