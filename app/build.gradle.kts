import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)

//    serialization
    kotlin("plugin.serialization") version "2.0.21"

//    ksp
    id("com.google.devtools.ksp")
}

android {
    namespace = "com.android.cyberqrscanner"
    compileSdk {
        version = release(37) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.android.cyberqrscanner"
        minSdk = 27
        targetSdk = 37
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    testImplementation(libs.junit)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    debugImplementation(libs.androidx.compose.ui.tooling)

//    Serialization
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.11.0")

//    Navigation
    implementation("androidx.navigation:navigation-compose:2.9.8")

//    Room DB
    implementation("androidx.room:room-runtime:2.8.4")

//    Kotlin Symbol Processing (KSP)
    ksp("androidx.room:room-compiler:2.8.4")

//    Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.11.0")

//    Extra Icons
    implementation("androidx.compose.material:material-icons-extended")

//    ML kit barcode scanner
    implementation("com.google.mlkit:barcode-scanning:17.3.0")

//    Viewmodel
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.10.0")

//    CameraX
    // The following line is optional, as the core library is included indirectly by camera-camera2
    implementation("androidx.camera:camera-core:1.6.1")
    implementation("androidx.camera:camera-camera2:1.6.1")
    // If you want to additionally use the CameraX Lifecycle library
    implementation("androidx.camera:camera-lifecycle:1.6.1")
    // If you want to additionally use the CameraX View class
    implementation("androidx.camera:camera-view:1.6.1")
    // If you want to additionally add CameraX ML Kit Vision Integration
    implementation("androidx.camera:camera-mlkit-vision:1.6.1")
    //
}