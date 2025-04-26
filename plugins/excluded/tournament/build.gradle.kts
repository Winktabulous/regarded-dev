plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.plugins.excluded"
version = "0.1.0"

dependencies {
    implementation(libs.apache.commons.lang3)

    implementation(projects.core)
}