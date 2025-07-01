rootProject.name = "template"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

include(
    ":client",
    ":server",
    ":common")
