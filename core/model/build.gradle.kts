plugins {
    id("foods.android.library")
}

android {
    namespace = "com.example.data"
}

dependencies {

    implementation(libs.kotlinx.datetime)
}