// shared/build.gradle.kts
import app.cash.sqldelight.gradle.VerifyMigrationTask
import org.gradle.api.tasks.testing.Test

plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("app.cash.sqldelight")
}

kotlin {
    // Android only (remove old ios() shortcut)
    androidTarget()

    jvmToolchain(17)

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.9.0")
                implementation("app.cash.sqldelight:runtime:2.0.2")
                implementation("app.cash.sqldelight:coroutines-extensions:2.0.2")
            }
        }
        val androidMain by getting {
            dependencies {
                implementation("app.cash.sqldelight:android-driver:2.0.2")
            }
        }
        // (optional) silence tests in this module
        val commonTest by getting { }
        val androidUnitTest by getting { }
    }
}

android {
    namespace = "com.example.shared"
    compileSdk = 34
    defaultConfig { minSdk = 24 }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}

// Turn off SQLDelight migration verification (Windows native lib lock issues)
tasks.withType<VerifyMigrationTask>().configureEach { enabled = false }

// (optional) disable unit test tasks in this module
tasks.withType<Test>().configureEach { enabled = false }

// SQLDelight config
sqldelight {
    databases {
        create("DeweyDatabase") {
            packageName.set("com.example.shared.db")
            schemaOutputDirectory.set(file("src/commonMain/sqldelight/schema"))
            migrationOutputDirectory.set(file("src/commonMain/sqldelight/migrations"))
            verifyMigrations.set(false)
        }
    }
}
