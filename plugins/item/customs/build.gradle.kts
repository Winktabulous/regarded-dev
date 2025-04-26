plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.plugins.item"
version = "0.1.0"

dependencies {
    implementation(projects.core)

    implementation(projects.plugins.item.definitions)
    implementation(projects.plugins.item.actions)
}
