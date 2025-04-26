@Suppress(
    "DSL_SCOPE_VIOLATION",
    "MISSING_DEPENDENCY_CLASS",
    "UNRESOLVED_REFERENCE_WRONG_RECEIVER",
    "FUNCTION_CALL_EXPECTED"
)
plugins {
    id("near-reality-project-kotlin")
    alias(libs.plugins.kotlin.serialization)
}

version = "0.1.0"
group = "com.near_reality.plugins.excluded.tools"

dependencies {
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.protobuf)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)
}
