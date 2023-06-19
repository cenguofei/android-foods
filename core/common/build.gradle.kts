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

    implementation(project(":core:model"))
}

//kapt {
//    correctErrorTypes = true
//}