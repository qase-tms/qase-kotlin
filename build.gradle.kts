plugins {
    java
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"
}

val gradleScriptDir by extra("${rootProject.projectDir}/gradle")

buildscript {
    repositories {
        mavenCentral()
        google()
        mavenLocal()
    }
    dependencies {
        classpath("org.jetbrains.kotlin:kotlin-gradle-plugin:1.9.24")
        classpath("org.jetbrains.kotlin:kotlin-android-extensions:1.9.24")
        classpath("com.android.tools.build:gradle:4.2.1")
    }
}

allprojects {
    group = "io.qase"
    version = "1.0.0"

    repositories {
        mavenCentral()
        google()
        mavenLocal()
        maven { url = uri("https://jitpack.io") }
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_1_8.toString()
        }
    }
}
