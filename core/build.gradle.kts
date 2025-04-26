plugins {
    id("near-reality-project-kotlin")
    id("org.jetbrains.kotlin.plugin.serialization") version "2.1.20"
}

group = "com.near_reality"
version = "0.1.0"

dependencies {
    api(projects.threads)
    api(projects.net)
    api(projects.cache)
    api(projects.coreModel)

    api(libs.kaml)
    api(libs.logback.classic)
    api(libs.fastutil)
    api(libs.google.guava)
    api(libs.kotlinx.coroutines.core)
    implementation(libs.kotlinx.serialization.core)
    api(libs.kotlinx.datetime)
    api(libs.classgraph)
    api(libs.okhttp)
    api(libs.checker.qual)
    api(libs.javaparser.core)
    api(libs.javaparser.symbol.solver.core)

    implementation(libs.apache.commons.lang3)
    implementation(libs.apache.commons.codec)
    implementation(libs.jctools.core)
    implementation(libs.jackson.dataformat.yaml)
    implementation(libs.jackson.module.afterburner)
    implementation(libs.jackson.module.kotlin)
    //implementation(libs.logstash.gelf)
    implementation(libs.mXparser)

    implementation(libs.bundles.ktor.server)
    implementation(libs.bundles.ktor.client)

    implementation(libs.bundles.exposed)
    implementation(libs.postgresql)

    implementation(libs.kord.core)

    implementation(libs.netty.codec.haproxy)

    implementation(libs.mockk)
}
