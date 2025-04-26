plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.plugins.item"
version = "0.1.0"

dependencies {
    api(projects.scripts.item.equip)
}
