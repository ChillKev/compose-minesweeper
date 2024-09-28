plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

group = "com.github.chillkev.minesweeper"
version = "1.1"

repositories {
}

dependencies {
    implementation(project(":common"))
    implementation(libs.activity.compose)
}

android {
    namespace = "com.github.chillkev.minesweeper.android"
    compileSdk = libs.versions.compileSdk.get().toInt()
    defaultConfig {
        applicationId = "com.github.chillkev.minesweeper.android"
        minSdk = libs.versions.minSdk.get().toInt()
        targetSdk = libs.versions.compileSdk.get().toInt()
        versionCode = 2
        versionName = "1.1"
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions.jvmTarget = "17"
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}