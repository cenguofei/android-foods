plugins {
    id("foods.android.library")
    id("foods.android.library.compose")

    id("foods.android.hilt")
}

android {
    namespace = "com.example.home"
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

    implementation(libs.kotlinx.coroutines.android)
    implementation(libs.androidx.lifecycle.viewmodel.ktx)
    implementation(libs.androidx.hilt.navigation.compose)

//    testImplementation(libs.test.junit)
//    androidTestImplementation(libs.androidx.test.ext.junit)
//    androidTestImplementation(libs.androidx.test.espresso.core)

    implementation(project(":core:designsystem"))
    implementation(project(mapOf("path" to ":core:network")))
    implementation(project(mapOf("path" to ":core:common")))
    implementation(project(mapOf("path" to ":core:model")))
    implementation(project(mapOf("path" to ":feature:login")))
}
kapt {
    correctErrorTypes = true
}