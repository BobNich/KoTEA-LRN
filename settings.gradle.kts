@file:Suppress("UnstableApiUsage")

rootProject.name = "tinkoff-kotea"

pluginManagement {
    repositories {
        gradlePluginPortal()
        google()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
}

include(":core")
include(":android")
include(":compose")
include(":logging")

include(":sample")
