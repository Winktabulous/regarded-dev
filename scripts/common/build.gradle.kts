plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality.scripts"
version = "0.1.0"

dependencies {
    api(projects.core)

    api(kotlin("scripting-common"))
    api(kotlin("scripting-jvm"))
    api(kotlin("scripting-jvm-host"))
}