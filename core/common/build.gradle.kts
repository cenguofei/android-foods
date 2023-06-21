plugins {
    id("foods.android.library")
    id("foods.android.hilt")
    //serialization
}

android {
    namespace = "com.example.common"
}

dependencies {

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation)

    implementation(project(":core:model"))
}

//kapt {
//    correctErrorTypes = true
//}