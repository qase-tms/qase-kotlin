description = "Qase Kotlin Android Integration"

plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish")
    signing
}

apply(plugin = "maven-publish")

repositories {
    mavenCentral()
    google()
    mavenLocal()
}

android {
    namespace = "io.qase.commons.kotlin.android"
    compileSdk = 34

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    api(project(":qase-kotlin-commons"))
    api(project(":qase-kotlin-junit4"))
    implementation("androidx.test.ext:junit:1.2.1")
    implementation("androidx.test:runner:1.6.2")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.test.uiautomator:uiautomator:2.3.0")
}

tasks.register<Javadoc>("androidJavadocs") {
    val androidLibrary = project.the(com.android.build.gradle.LibraryExtension::class)

    source(androidLibrary.sourceSets["main"].java.srcDirs)
    classpath += project.files(androidLibrary.bootClasspath.joinToString(File.pathSeparator))
    androidLibrary.libraryVariants.find { it.name == "release" }?.apply {
        classpath += javaCompileProvider.get().classpath
    }

    exclude("**/R.html", "**/R.*.html", "**/index.html")

    val stdOptions = options as StandardJavadocDocletOptions
    stdOptions.addBooleanOption("Xdoclint:-missing", true)
    stdOptions.links(
        "http://docs.oracle.com/javase/7/docs/api/",
        "http://developer.android.com/reference/",
        "http://hc.apache.org/httpcomponents-client-5.0.x/httpclient5/apidocs/",
        "http://hc.apache.org/httpcomponents-core-5.0.x/httpcore5/apidocs/"
    )
}

tasks.register<Jar>("androidJavadocsJar") {
    val javadocTask = tasks.getByName<Javadoc>("androidJavadocs")
    dependsOn(javadocTask)
    archiveClassifier.set("javadoc")
    from(javadocTask.destinationDir)
}

tasks.register<Jar>("androidSourcesJar") {
    archiveClassifier.set("sources")
    from(android.sourceSets["main"].java.srcDirs)
}

afterEvaluate {
    println(components.names)

    publishing {
        publications {
            create<MavenPublication>("maven") {
                from(components["release"])
                artifact(tasks.getByName<Jar>("androidJavadocsJar"))

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

    signing {
        sign(publishing.publications["maven"])
    }
}
