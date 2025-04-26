plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.plugins"
version = "0.1.0"

dependencies {
    api(projects.scripts.shops)
    implementation(kotlin("script-runtime"))
}