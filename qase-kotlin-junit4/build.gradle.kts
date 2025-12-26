description = "Qase Kotlin JUnit 4 Integration"

plugins {
    kotlin("jvm") version "1.9.24"
}

dependencies {
        api(project(":qase-kotlin-commons"))
    implementation("junit:junit:4.13.2")
    testImplementation(kotlin("test"))
}

tasks.test {
    useJUnitPlatform()
}
