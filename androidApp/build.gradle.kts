import org.gradle.api.tasks.testing.Test

tasks.withType<Test>().configureEach {
    enabled = false
}


plugins {
    id("com.android.application")
    kotlin("android")
    alias(libs.plugins.compose.compiler)
}

android {
    namespace = "com.example.androidApp"
    compileSdk = 35
    testOptions {
        // Disable all JVM unit tests for this Android library module
        unitTests.all {
            it.enabled = false
        }
    }
    defaultConfig {
        applicationId = "com.example.androidApp"
        minSdk = 24
        targetSdk = 35
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }

    buildFeatures { compose = true }
    composeOptions { kotlinCompilerExtensionVersion = "1.5.14" }
}

dependencies {
    implementation(project(":shared"))

    // Compose core
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("androidx.compose.ui:ui:1.7.3")
    implementation("androidx.compose.material3:material3:1.3.0")

    // Lifecycle
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.4")
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.4") // <- needed for viewModel()

    // Compose tooling (optional but very helpful)
    implementation("androidx.compose.ui:ui-tooling-preview:1.7.3")
    debugImplementation("androidx.compose.ui:ui-tooling:1.7.3")
    implementation("androidx.compose.runtime:runtime-saveable:1.7.3")

    implementation("androidx.compose.ui:ui-text:1.7.3")
    implementation("androidx.compose.foundation:foundation:1.7.3")


}

