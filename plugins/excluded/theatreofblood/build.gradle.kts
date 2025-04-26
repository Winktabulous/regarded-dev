plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.plugins.excluded"
version = "0.1.0"

dependencies {
    implementation(projects.core)

    implementation(projects.scripts.interfaces)
    implementation(projects.scripts.npc.drops)
    implementation(projects.scripts.item.actions)

    implementation(libs.mockk)
}
