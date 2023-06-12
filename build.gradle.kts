// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }

//    dependencies {
//        classpath("com.google.dagger:hilt-android-gradle-plugin:2.44.2")
//    }
}

// Lists all plugins used throughout the project without applying them.
plugins {
    id("com.android.application").version("8.0.1").apply(false)
    id("com.android.library").version("8.0.1").apply(false)
    id("org.jetbrains.kotlin.android").version("1.7.20").apply(false)
    //hilt
    id("com.google.dagger.hilt.android").version("2.44").apply(false)

}