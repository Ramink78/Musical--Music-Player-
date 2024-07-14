plugins {
    alias(libs.plugins.musical.libraryCompose)
    alias(libs.plugins.musical.hilt)
}

android {
    namespace = "rk.core"
}

dependencies {

    //Media3
    api(libs.androidx.media3.exoplayer)

    implementation(libs.coil.compose)
    implementation(libs.androidx.material.icons.extended)
}