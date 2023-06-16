plugins {
    id("foods.android.library")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.model"
}

dependencies {

    implementation(libs.kotlinx.datetime)
}
