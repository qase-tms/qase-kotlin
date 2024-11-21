import java.time.Duration

plugins {
    java
    signing
    id("maven-publish")

    kotlin("jvm") version "1.9.24"
    kotlin("plugin.serialization") version "1.9.24"

    id("io.github.gradle-nexus.publish-plugin") version "1.1.0"
}

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

val gradleScriptDir by extra("${rootProject.projectDir}/gradle")

nexusPublishing {
    connectTimeout.set(Duration.ofMinutes(7))
    clientTimeout.set(Duration.ofMinutes(7))

    transitionCheckOptions {
        maxRetries.set(100)
        delayBetween.set(Duration.ofSeconds(10))
    }

    repositories {
        sonatype()
    }
}

allprojects {
    group = "io.qase"
    version = "1.0.0"

    repositories {
        mavenCentral()
        google()
        mavenLocal()
    }

    tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile>().configureEach {
        kotlinOptions {
            jvmTarget = JavaVersion.VERSION_17.toString()
        }
    }
}

configure(
    subprojects
    .filter { !it.name.contains("android") }
    .filter { !it.name.contains("kaspresso") }
) {
    apply(plugin = "signing")
    apply(plugin = "maven-publish")
    apply(plugin = "org.jetbrains.kotlin.jvm")

    dependencies {
        implementation(kotlin("stdlib"))
    }

    tasks.jar {
        manifest {
            attributes(
                mapOf(
                    "Implementation-Title" to project.name,
                    "Implementation-Version" to project.version
                )
            )
        }
    }

    val sourceJar by tasks.creating(Jar::class) {
        from(sourceSets.getByName("main").allSource)
        archiveClassifier.set("sources")
    }

    val javadocJar by tasks.creating(Jar::class) {
        from(tasks.getByName("javadoc"))
        archiveClassifier.set("javadoc")
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
                artifact(javadocJar)
                artifact(sourceJar)
                from(components["java"])

                pom {
                    name.set(project.name)
                    description.set("Module ${project.name} of Qase Kotlin reporters.")
                    url.set("https://github.com/qase-tms/qase-kotlin")
                    licenses {
                        license {
                            name.set("The Apache License, Version 2.0")
                            url.set("http://www.apache.org/licenses/LICENSE-2.0.txt")
                        }
                    }
                    developers {
                        developer {
                            id.set("qase-tms")
                            name.set("Qase Team")
                            email.set("support@qase.io")
                        }
                    }
                    scm {
                        developerConnection.set("scm:git:git://github.com/qase-tms/qase-kotlin")
                        connection.set("scm:git:git://github.com/qase-tms/qase-kotlin")
                        url.set("https://github.com/qase-tms/qase-kotlin")
                    }
                    issueManagement {
                        system.set("GitHub Issues")
                        url.set("https://github.com/qase-tms/qase-kotlin/issue")
                    }
                }
            }
        }
    }

//    signing {
//        val signingKeyId: String? by project
//        val signingKey: String? by project
//        val signingPassword: String? by project
//        useInMemoryPgpKeys(signingKeyId, signingKey, signingPassword)
//
//        sign(publishing.publications["maven"])
//    }
}

tasks.withType<org.jetbrains.kotlin.gradle.tasks.KotlinJvmCompile>().configureEach {
    jvmTargetValidationMode.set(org.jetbrains.kotlin.gradle.dsl.jvm.JvmTargetValidationMode.WARNING)
}
