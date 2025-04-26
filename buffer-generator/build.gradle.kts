plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality"
version = "0.1.0"

dependencies {
    api(libs.clikt)

    implementation(projects.util)
    implementation(libs.netty.buffer)
    implementation(libs.kotlinpoet)
}