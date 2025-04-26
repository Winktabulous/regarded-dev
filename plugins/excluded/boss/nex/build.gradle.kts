plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.plugins.excluded.boss"
version = "0.1.0"

dependencies {
    implementation(projects.core)

    implementation(projects.scripts.npc.actions)
    implementation(projects.scripts.npc.definitions)
    implementation(projects.scripts.npc.drops)

    implementation(projects.scripts.item.actions)
    implementation(projects.scripts.item.equip)
    implementation(projects.scripts.item.definitions)

    implementation(projects.scripts.`object`.actions)
}
