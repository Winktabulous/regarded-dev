plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.plugins.excluded.boss"
version = "0.1.0"

dependencies {
    implementation(projects.core)

    implementation(projects.scripts.npc.actions)
	implementation(kotlin("script-runtime"))
}