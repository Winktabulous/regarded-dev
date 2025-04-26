plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.plugins.area.osnr_home"
version = "0.1.0"

dependencies {
    implementation(projects.core)
    implementation(projects.scripts.npc.actions)
    implementation(projects.scripts.npc)
    implementation(projects.scripts.npc.definitions)
}
