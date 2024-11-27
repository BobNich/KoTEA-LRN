plugins {
    id("kotlin")
    alias(libs.plugins.gradle.binary.validator)
}

dependencies {
    implementation(libs.kotlinx.coroutines.core)

    testImplementation(libs.test.junit4)
    testImplementation(libs.test.turbine)
}
