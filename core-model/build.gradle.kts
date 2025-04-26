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

group = "com.near_reality"
version = "0.1.0"

dependencies {
    api(projects.api)
    api(libs.fastutil)

    implementation(libs.google.gson)
    implementation(libs.slf4j.api)
    implementation(libs.netty.buffer)

    implementation(libs.kotlinx.serialization.core)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.serialization.protobuf)

    implementation(libs.kotlinx.datetime)

    implementation(libs.bundles.exposed)

    implementation(libs.jackson.core)
    implementation(libs.jackson.databind)
    implementation(libs.jackson.dataformat.yaml)
    implementation(libs.jackson.datatype.jsr310)
    implementation(libs.jackson.module.kotlin)

    implementation(libs.apache.commons.lang3)
    implementation(libs.apache.commons.io)
}
