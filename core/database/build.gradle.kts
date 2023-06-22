plugins {
    id("foods.android.library")
    id("foods.android.library.compose")
    id("foods.android.hilt")
    id("kotlin-kapt")
}

android {
    namespace = "com.example.database"
}

dependencies {


    implementation(project(mapOf("path" to ":core:common")))
    val room_version = "2.5.1"
    implementation("androidx.room:room-runtime:$room_version")
//    annotationProcessor("androidx.room:room-compiler:$room_version")

    // To use Kotlin annotation processing tool (kapt)
    kapt("androidx.room:room-compiler:$room_version")

    // optional - Kotlin Extensions and Coroutines support for Room
    implementation("androidx.room:room-ktx:$room_version")
}