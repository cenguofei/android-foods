plugins {
    id("foods.android.library")
    id("foods.android.library.compose")
}

android {
    namespace = "com.example.start"
}

dependencies {


    implementation(libs.androidx.compose.material)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.tooling)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(project(":core:designsystem"))
    implementation(project(":core:datastore"))

    implementation(libs.lottie.compose)
}