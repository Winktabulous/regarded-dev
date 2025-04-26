plugins {
    kotlin("multiplatform")
    kotlin("plugin.serialization")
}

group = "com.near_reality"
version = "3.6.0"

kotlin {
    jvmToolchain(21)

    jvm()
    /*js {
        browser()
        nodejs()
        binaries.executable()
    }*/

    sourceSets {
        val commonMain by getting {
            dependencies {
                implementation(libs.ktor.client.resources)
                implementation(libs.kotlinx.serialization.json)
                implementation(libs.kotlinx.datetime)
            }
        }
        val jvmMain by getting {
            dependencies {
                implementation(libs.exposed.core)
                implementation(libs.exposed.dao)
                implementation(libs.exposed.jdbc)
                implementation(libs.exposed.json)
                implementation(libs.exposed.kotlin.datetime)

                implementation(libs.ktor.client.auth)
                implementation(libs.ktor.client.content.negotiation)
                implementation(libs.ktor.client.logging)
                implementation(libs.ktor.serialization.kotlinx.json)

                implementation(libs.hikari.cp)
            }
        }
    }
}
