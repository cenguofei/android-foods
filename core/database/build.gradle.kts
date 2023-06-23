plugins {
    id("foods.android.library")
    id("foods.android.library.compose")
    id("foods.android.hilt")
    id("foods.android.room")
}

android {
    namespace = "com.example.database"
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

    implementation(project(":core:model"))
}