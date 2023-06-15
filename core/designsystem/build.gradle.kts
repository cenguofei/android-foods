
plugins {

//    id("foods.android.application")
//    id("foods.android.application.compose")

    id("foods.android.library")
    id("foods.android.library.compose")
}

android {
    namespace = "com.example.designsystem"
}

dependencies {


    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material.iconsExtended)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.coil.kt.compose)
    implementation(project(mapOf("path" to ":core:model")))
}