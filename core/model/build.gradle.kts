plugins {
    id("foods.android.library")
    id("kotlin-parcelize")
//    id("kotlin-kapt")
}

android {
    namespace = "com.example.model"
}

dependencies {

    implementation(libs.kotlinx.datetime)

//    implementation(libs.androidx.room.runtime)
//    annotationProcessor(libs.androidx.room.compiler)

//    kapt(libs.androidx.room.compiler)
//    ksp(libs.androidx.room.compiler)

//    implementation(libs.androidx.room.ktx)
}
