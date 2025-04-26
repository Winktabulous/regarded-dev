plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.plugins"
version = "0.1.0"

dependencies {
    implementation(projects.core)

    implementation(libs.apache.commons.lang3)
}