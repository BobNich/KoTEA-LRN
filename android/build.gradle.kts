plugins {
    alias(libs.plugins.gradle.android.library)
    alias(libs.plugins.gradle.kotlin)
    alias(libs.plugins.gradle.binary.validator)
}

android {
    namespace = "ru.tinkoff.kotea.android"
}

dependencies {
    api(projects.core)

    implementation(libs.androidx.fragment.ktx)
    implementation(libs.androidx.lifecycle.runtime)
    implementation(libs.androidx.lifecycle.viewmodel)
}
