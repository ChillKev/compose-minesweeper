import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    kotlin("multiplatform")
    alias(libs.plugins.jetbrains.compose)
    alias(libs.plugins.compose.compiler)
}

group = "com.github.chillkev.minesweeper"
version = "1.1"

kotlin {
    jvmToolchain(17)
    jvm {
        compilations.all {
            compileTaskProvider {
                compilerOptions.jvmTarget.set(JvmTarget.JVM_17)
            }
        }
        withJava()
    }

    val osName = System.getProperty("os.name")
    val targetOs = when {
        osName == "Mac OS X" -> "macos"
        osName.startsWith("Win") -> "windows"
        osName.startsWith("Linux") -> "linux"
        else -> error("Unsupported OS: $osName")
    }

    val targetArch = when (val osArch = System.getProperty("os.arch")) {
        "x86_64", "amd64" -> "x64"
        "aarch64" -> "arm64"
        else -> error("Unsupported arch: $osArch")
    }

    val version = "0.8.10" // or any more recent version
    val target = "${targetOs}-${targetArch}"

    sourceSets {
        val jvmMain by getting {
            dependencies {
                implementation(project(":common"))
                implementation("org.jetbrains.skiko:skiko-awt-runtime-$target:$version")
                implementation(compose.desktop.currentOs)
            }
        }
        val jvmTest by getting
    }
}

compose.desktop {
    application {
        mainClass = "MainKt"
        nativeDistributions {
            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
            packageName = "jvm"
            packageVersion = "1.0.0"
        }
    }
}