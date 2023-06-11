plugins {
    id("foods.android.library")
    id("foods.android.hilt")
    //serialization
//    id("foods.kotlin.serialization")

//    kotlin("plugin.serialization") version "1.8.22"
}

android {
    namespace = "com.example.data"
}

dependencies {

//    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
//    implementation(libs.kotlinx.datetime)
//    implementation(libs.kotlinx.serialization.json)

//    implementation(libs.square.retrofit2)
//    implementation(libs.square.retrofit2.converter.gson)
}