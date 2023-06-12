plugins {
    id("foods.android.library")
    id("foods.android.hilt")
}

android {
    namespace = "com.example.data"
}

dependencies {

    implementation(libs.androidx.datastore.preferences)
    implementation(libs.kotlinx.coroutines.android)
}

kapt {
    correctErrorTypes = true
}