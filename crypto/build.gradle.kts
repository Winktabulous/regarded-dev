plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality"
version = "0.1.0"

dependencies {
    api(libs.netty.buffer)

    implementation(projects.util)
}