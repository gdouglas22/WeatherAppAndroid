plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.weatherapp"
    compileSdk = 35

    buildFeatures {
        dataBinding = true
        viewBinding = rootProject.extra["viewBindingEnabled"] as Boolean
    }

    defaultConfig {
        applicationId = "com.example.weatherapp"
        minSdk = 29
        targetSdk = 35
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
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.material.v180)
    implementation(libs.squareup.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.okhttp)
    implementation(libs.graphview)
    implementation(libs.core)
    implementation(libs.viewpager2)
    implementation(libs.play.services.location)
    implementation(libs.lifecycle.runtime.ktx)
    implementation(libs.lifecycle.runtime.ktx.v240alpha03)

    val lifecycle_version = "2.8.7"
    val arch_version = "2.2.0"

    implementation(libs.lifecycle.viewmodel.ktx)
    testImplementation (libs.lifecycle.runtime.testing)
    implementation(libs.lifecycle.livedata)
    implementation(libs.lifecycle.runtime)
}