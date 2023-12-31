plugins {
    id("foods.android.library")
    id("foods.android.hilt")
}

android {
    namespace = "com.example.common"
}

dependencies {

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(project(":core:model"))
    implementation(project(":core:database"))
}
