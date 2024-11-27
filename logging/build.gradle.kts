plugins {
    id("kotlin")
    alias(libs.plugins.gradle.binary.validator)
}

dependencies {
    api(project(":core"))
}
