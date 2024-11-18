plugins {
    java
    kotlin("jvm") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"
    id("maven-publish")
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
        classpath("com.android.tools.build:gradle:8.6.1")
    }
}

allprojects {
    group = "io.qase"
    version = "1.0.0"

    repositories {
        mavenCentral()
        mavenLocal()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }
}

configure(subprojects.filter { !it.name.contains("android") }
    .filter { !it.name.contains("kaspresso") }) {
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        implementation(kotlin("stdlib"))
    }

    tasks.jar {
        manifest {
            attributes(mapOf(
                "Implementation-Title" to project.name,
                "Implementation-Version" to project.version
            ))
        }
    }

    val sourceJar by tasks.creating(Jar::class) {
        from(sourceSets.getByName("main").allSource)
        archiveClassifier.set("sources")
    }

    tasks.withType(Javadoc::class) {
        (options as StandardJavadocDocletOptions).addStringOption("Xdoclint:none", "-quiet")
    }

    configure<JavaPluginConvention> {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    publishing {
        publications {
            create<MavenPublication>("mavenJava") {
                from(components["java"])
            }
        }
        repositories {
            mavenLocal()
        }
    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.WARNING)
}
