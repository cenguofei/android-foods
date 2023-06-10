plugins {
    id("foods.android.library")
    id("foods.android.hilt")
}

android {
    namespace = "com.example.data"
}

dependencies {

//    implementation(libs.androidx.core.ktx)
    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
}