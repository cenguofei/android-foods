plugins {
    id("foods.android.library")
}

android {
    namespace = "com.example.lwh"
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.google.android.material)

    testImplementation(libs.test.junit)
    androidTestImplementation(libs.androidx.test.ext.junit)
    androidTestImplementation(libs.androidx.test.espresso.core)
    implementation(project(":core:model"))
    implementation(project(":core:common"))
}