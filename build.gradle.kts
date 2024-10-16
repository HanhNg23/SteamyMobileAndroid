// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    repositories {
        google()
        mavenCentral()
    }
    dependencies {
        val nav_version = "2.8.2"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$nav_version")
        classpath("com.android.tools.build:gradle:8.6.0")
        classpath("com.google.protobuf:protobuf-gradle-plugin:0.9.4")
        classpath("com.apollographql.apollo:apollo-gradle-plugin:4.0.0")

    }
}
plugins {
    alias(libs.plugins.android.application) apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
    id("com.apollographql.apollo") version "4.0.0"
}


