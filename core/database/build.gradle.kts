plugins {
    id("foods.android.library")
    id("foods.android.hilt")
}

android {
    namespace = "com.example.database"
}

dependencies {
    implementation(libs.androidx.room.runtime)
    annotationProcessor(libs.androidx.room.compiler)

//    kapt(libs.androidx.room.compiler)
//    ksp(libs.androidx.room.compiler)

    implementation(libs.androidx.room.ktx)
    implementation(project(mapOf("path" to ":core:model")))
}