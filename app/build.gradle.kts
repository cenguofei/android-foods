plugins {
    id("foods.android.application")
    id("foods.android.application.compose")
    id("foods.android.hilt")

//    kotlin("kapt")
//    id("com.google.dagger.hilt.android")
}

android {
    namespace = "com.example.namespace"

//    compileSdk = 33

    defaultConfig {
        applicationId = "cn.example.foods"
//        minSdk = 29
//        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
//    compileOptions {
//        sourceCompatibility = JavaVersion.VERSION_11
//        targetCompatibility = JavaVersion.VERSION_11
//    }

    packaging {
        resources {
            excludes.add("/META-INF/{AL2.0,LGPL2.1}")
        }
    }
}

dependencies {

    implementation(libs.google.android.material)
    implementation(libs.androidx.constraintlayout)
    implementation(libs.androidx.lifecycle.livedata.ktx)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.test.espresso.core)
    implementation(libs.okhttp)
    implementation(libs.okhttp.logging)
    implementation(libs.google.gson)
    implementation(libs.github.glide)
    androidTestImplementation(libs.androidx.test.ext)


    //compose
    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.ui.util)
    implementation(libs.androidx.compose.ui.test.junit4)
    implementation(libs.androidx.compose.ui.test.manifest)

    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.viewmodel.compose)
//    implementation(libs.androidx.lifecycle.livedata)

    implementation(libs.accompanist.systemuicontroller)


    implementation(project(":core:designsystem"))
    implementation(project(":core:model"))
//    implementation("com.google.dagger:hilt-android:2.44")
//    annotationProcessor("com.google.dagger:hilt-android-compiler:2.44")
}