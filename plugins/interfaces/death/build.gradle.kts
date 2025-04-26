plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.plugins.interfaces"
version = "0.1.0"

dependencies {
    implementation(projects.scripts.interfaces)
}