plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "0.8.0"
}
rootProject.name = "qase-kotlin"
include("qase-kotlin-commons")
include("qase-kotlin-junit4")
include("qase-kotlin-android")
include("qase-kaspresso-reporter")
