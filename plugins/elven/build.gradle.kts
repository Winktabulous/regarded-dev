plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.plugins"
version = "0.1.0"

dependencies {
    implementation(projects.core)
    implementation(projects.scripts.`object`.actions)
    implementation(projects.scripts.npc.actions)
    implementation(projects.scripts.item.definitions)
    implementation(projects.scripts.item.actions)
}
