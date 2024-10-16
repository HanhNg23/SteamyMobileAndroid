plugins {
    alias(libs.plugins.android.application)
    id("com.google.dagger.hilt.android")
    id("com.apollographql.apollo") version "4.0.0"
    id("androidx.navigation.safeargs")
    id("com.google.protobuf")
    id("kotlin-parcelize")

}

android {
    namespace = "com.stemy.mobileandroid"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.stemy.mobileandroid"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    buildFeatures {
        viewBinding = true
    }
    apollo {
        service("service") {
            srcDir("src/main/graphql/src/main/graphql")
            packageName.set("com.stemy.mobileandroid")
            mapScalar("File", "com.apollographql.apollo.api.Upload")

        }
    }
}

protobuf {
    protoc {
        artifact = "com.google.protobuf:protoc:3.25.1"
    }
    plugins {
        create("grpc") {
            artifact = "io.grpc:protoc-gen-grpc-java:1.43.0"
        }
    }
    generateProtoTasks {
        all().forEach { task ->
            task.builtins {
                create("java") {
                    option("lite")
                }
            }
            task.plugins {
                create("grpc") {
                    option("lite")
                }
            }
        }
    }
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.legacy.support.v4)
    implementation(libs.recyclerview)
    implementation(libs.annotation)
    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.material)
    implementation(libs.com.google.dagger.hilt.android.gradle.plugin)
    implementation(libs.retrofit)
    implementation(libs.converter.gson)
    implementation(libs.lombok)
    implementation(libs.client)
    implementation(libs.rx3)
    implementation(libs.rxjava)
    implementation(libs.rxandroid)
    implementation(libs.grpc.okhttp)
    implementation(libs.grpc.protobuf.lite)
    implementation(libs.grpc.stub)
    implementation(libs.annotations.api)
    implementation(libs.protobuf.gradle.plugin)
    implementation(libs.ads.mobile.sdk)
    implementation(libs.glide)
//    implementation(libs.org.jetbrains.kotlin.plugin.parcelize.gradle.plugin)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation(libs.hilt.android)
    annotationProcessor(libs.hilt.compiler)
    implementation(libs.apollo.runtime)
    compileOnly(libs.lombok)
    annotationProcessor(libs.lombok)
}
