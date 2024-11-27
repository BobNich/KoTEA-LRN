plugins {
    alias(libs.plugins.gradle.android.application)
    alias(libs.plugins.gradle.kotlin)
}

android {
    namespace = "ru.tinkoff.mobile.kotea.sample"
    compileSdk = 35

    defaultConfig {
        applicationId = "ru.tinkoff.mobile.kotea.sample"
        minSdk = 21
        targetSdk = 35
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions.jvmTarget = JavaVersion.VERSION_17.toString()

    buildTypes {
        release {
            isMinifyEnabled = false
        }
    }

    buildFeatures {
        viewBinding = true
    }
}

dependencies {
    implementation(projects.android)
    implementation(projects.logging)

    implementation(libs.androidx.appcompat)
}