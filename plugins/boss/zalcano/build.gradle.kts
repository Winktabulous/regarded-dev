plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.plugins.boss"
version = "0.1.0"

dependencies {
    implementation(projects.plugins.rewards)
    implementation(projects.core)
}