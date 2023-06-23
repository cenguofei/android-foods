plugins {
    id("foods.android.library")
    id("kotlin-parcelize")
    id("foods.android.room")
}

android {
    namespace = "com.example.model"
}

dependencies {

    implementation(libs.kotlinx.datetime)
}
