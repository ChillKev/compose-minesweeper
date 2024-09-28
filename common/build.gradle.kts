import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
    alias(libs.plugins.sqldelight.gradle)
}

group = "com.github.chillkev.minesweeper"
version = "1.1"

sqldelight {
    databases {
        create("MinesweeperDatabase") {
            packageName.set("com.github.chillkev.minesweeper")
            dialect(libs.sqldelight.dialects.sqlite)
        }
    }
}

kotlin {
    androidTarget {
        compilations.all {
            compileTaskProvider {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }
    jvm("desktop") {
        compilations.all {
            compileTaskProvider {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }
        }
    }
    sourceSets {
        val commonMain by getting {
            dependencies {
                api(compose.runtime)
                api(compose.foundation)
                api(compose.material)
                api(libs.kotlinx.coroutines.core)
                implementation(compose.components.resources)
                implementation(libs.sqldelight.coroutines.extensions)
                implementation(libs.sqldelight.primitive.adapters)
            }
        }
        val commonTest by getting {
            dependencies {
                implementation(kotlin("test"))
            }
        }
        val androidMain by getting {
            dependencies {
                api(libs.appcompat)
                api(libs.core.ktx)
                implementation(libs.sqldelight.android.driver)
            }
        }
        val androidUnitTest by getting {
            dependencies {
                implementation(libs.junit)
            }
        }
        val desktopMain by getting {
            dependencies {
                api(compose.preview)
                implementation(libs.sqldelight.sqlite.driver)
            }
        }
        val desktopTest by getting
    }
}

android {
    namespace = "com.github.chillkev.minesweeper.common"
    compileSdk = libs.versions.compileSdk.get().toInt()
    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    defaultConfig {
        minSdk = libs.versions.minSdk.get().toInt()
        testOptions.targetSdk = libs.versions.compileSdk.get().toInt()
        lint.targetSdk = libs.versions.compileSdk.get().toInt()
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}
