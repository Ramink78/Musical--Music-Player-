plugins {
    alias(libs.plugins.musical.feature)
}

android {
    namespace = "rk.ui.nowplaying"
}

dependencies {
    implementation(libs.coil.compose)
    implementation(libs.androidx.palette.ktx)
}