// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.jetbrainsKotlinAndroid) apply false
    id("com.android.library") version "8.0.0" apply false
    id("com.google.gms.google-services") version "4.4.1" apply false
    id("com.google.dagger.hilt.android") version "2.51.1" apply false
}
buildscript {
    dependencies {
        classpath("com.google.android.libraries.mapsplatform.secrets-gradle-plugin:secrets-gradle-plugin:2.0.1")
    }
}
