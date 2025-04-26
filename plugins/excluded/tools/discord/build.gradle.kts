@Suppress(
    "DSL_SCOPE_VIOLATION",
    "MISSING_DEPENDENCY_CLASS",
    "UNRESOLVED_REFERENCE_WRONG_RECEIVER",
    "FUNCTION_CALL_EXPECTED"
)
plugins {
    id("near-reality-project-kotlin")
}

version = "0.1.0"
group = "com.near_reality.plugins.excluded.tools"

dependencies {
    implementation(projects.core)
    implementation(libs.kotlinx.datetime)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.ktor.client.auth)
    implementation(libs.ktor.client.logging)
    implementation(libs.kotlinx.atomicfu)
    implementation(libs.kord.core)
}
