plugins {
    id("foods.android.library")
}

android {
    namespace = "com.example.model"
}

dependencies {

    implementation(libs.kotlinx.datetime)
}