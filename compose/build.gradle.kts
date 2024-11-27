plugins {
    alias(libs.plugins.gradle.android.library)
    alias(libs.plugins.gradle.kotlin)
    alias(libs.plugins.gradle.binary.validator)
}

android {
    namespace = "ru.tinkoff.kotea.compose"

    buildFeatures {
        compose = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = libs.versions.compose.compiler.get()
    }
}

dependencies {
    api(project(":android"))
    api(project(":core"))

    implementation(libs.compose.lifecycle)
    implementation(libs.compose.ui)
}