plugins {
    alias(libs.plugins.musical.application)
    alias(libs.plugins.musical.applicationCompose)
    alias(libs.plugins.musical.hilt)
    alias(libs.plugins.ktlint)
    alias(libs.plugins.musical.room)
}

android {
    namespace = "rk.musical"
}
tasks.getByName("preBuild").dependsOn("ktlintFormat")
ktlint {
    android = true
    ignoreFailures = false
    reporters {
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.PLAIN)
        reporter(org.jlleitschuh.gradle.ktlint.reporter.ReporterType.CHECKSTYLE)
    }
}

dependencies {
    // Domains
    implementation(projects.domain.songs)
    implementation(projects.domain.albums)

    // Features
    implementation(projects.feature.nowPlaying)
    implementation(projects.feature.songs)
    implementation(projects.feature.playlist)
    implementation(projects.feature.playlistDetail)
    implementation(projects.feature.albums)
    implementation(projects.feature.albumDetail)

    implementation(projects.playbackService)
    implementation(projects.core)
    implementation(libs.androidx.lifecycle.runtime.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.hilt.navigation.compose)
    implementation(libs.accompanist.permissions)
    implementation(libs.androidx.palette.ktx)
}
