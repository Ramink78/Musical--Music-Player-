pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url= uri("https://jitpack.io") }    }
}
rootProject.name = "Musical"
include(":app")
include(":PlaybackService")
include(":data:songs")
include(":feature:songs")
include(":core")
include(":domain:songs")
include(":data:albums")
include(":domain:albums")
include(":feature:nowPlaying")
include(":data:playlist")
include(":domain:playlist")
include(":feature:playlist")
include(":data:model")
include(":feature:playlist-detail")
include(":feature:albums")
