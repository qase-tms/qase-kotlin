plugins {
    id("com.android.library")
    kotlin("android")
    id("maven-publish") // Добавляем плагин публикации
}

apply(plugin = "maven-publish")

repositories {
    mavenCentral()
    google()
    mavenLocal()
}

android {
    namespace = "io.qase.commons.kotlin.android.qasekotlinandroid"
    compileSdkVersion(30)

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
}

dependencies {
    implementation("androidx.test.ext:junit:1.1.2")
    implementation("androidx.test:runner:1.4.0")
    implementation("androidx.multidex:multidex:2.0.1")
    implementation("androidx.test.uiautomator:uiautomator:2.2.0")
    implementation(project(":qase-kotlin-commons"))
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
        "http://hc.apache.org/httpcomponents-core-5.0.x/httpcore5/apidocs/")
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

publishing {
    publications {
        create<MavenPublication>("mavenAndroid") {
            afterEvaluate {
                from(components["release"])
            }
            groupId = "io.qase"
            artifactId = "qase-kotlin-android"
            version = "1.0.0"
        }
    }
    repositories {
        mavenLocal()
    }
}
