plugins {
    alias(libs.plugins.musical.library)
    alias(libs.plugins.musical.hilt)
}

android {
    namespace = "rk.data.albums"
}

dependencies {
    implementation(project(":core"))
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
}