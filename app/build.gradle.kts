import com.android.build.api.dsl.Packaging
import org.gradle.api.internal.artifacts.configurations.Configurations
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories
import org.gradle.internal.impldep.org.junit.experimental.categories.Categories.CategoryFilter.exclude
import org.jetbrains.kotlin.ir.linkage.partial.PartialLinkageUtils.File.MissingDeclarations.module

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsKotlinAndroid)
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-android")

}

android {
    namespace = "com.example.geminiwithclaude"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.geminiwithclaude"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.2"
    }

    kotlinOptions {
        jvmTarget = "1.8"
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
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
        dataBinding = true

    }

    //Because there have two dependencies, which the module have the same classes, this make 編譯器couldn't successfully
    //run, so you should exclude one of the module to make the duplicate classes disappear
    //why using syntex of "all". Because I can't find where the dependencies including protobuf-java is.
    configurations {
        all {
            exclude(group = "com.google.protobuf", module = "protobuf-java")
        }
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.tools.core)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("androidx.vectordrawable:vectordrawable-animated:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.browser:browser:1.5.0")
    implementation("com.google.android.material:material:1.11.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.7")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.7")
    implementation("androidx.hilt:hilt-navigation-compose:1.0.0")
    implementation("com.google.dagger:hilt-android:2.51.1")
    kapt("com.google.dagger:hilt-compiler:2.51.1")

    implementation("com.github.bumptech.glide:glide:4.16.0")

    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation("androidx.activity:activity-compose:1.6.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    implementation("com.google.ai.client.generativeai:generativeai:0.2.2")
    implementation (platform("com.google.firebase:firebase-bom:32.8.0"))
    implementation("com.google.firebase:firebase-firestore-ktx:24.11.0")
    implementation("com.google.android.gms:play-services-auth:21.0.0")
    implementation("com.firebaseui:firebase-ui-auth:8.0.2")
    implementation("com.google.firebase:firebase-auth-ktx")
    implementation("androidx.startup:startup-runtime:1.1.1")
    implementation("com.google.firebase:firebase-auth:22.3.1")
    implementation ("com.firebaseui:firebase-ui-auth:7.2.0")
    
    implementation ("com.github.ankidroid:Anki-Android:api-v1.1.0")


}
