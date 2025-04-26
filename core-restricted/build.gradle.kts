plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality"
version = "0.1.0"

dependencies {

    api(projects.threads)
    api(projects.net)
    api(projects.cache)
    api(projects.coreModel)
    api(projects.core)

    implementation(projects.scripts.npc.spawns)
    implementation(projects.scripts.npc)
    implementation(projects.scripts.npc.actions)
    implementation(projects.scripts.npc.definitions)
    implementation(projects.scripts.npc.drops)
    implementation(projects.scripts.interfaces)

    implementation(projects.scripts.groundItems)

    implementation(projects.scripts.item.actions)
    implementation(projects.scripts.item.equip)
    implementation(projects.scripts.item.definitions)

    implementation(projects.scripts.`object`.actions)

    api(libs.logback.classic)
    api(libs.fastutil)
    api(libs.google.guava)
    api(libs.kotlinx.coroutines.core)
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

    implementation(libs.ktor.client.core)
    implementation(libs.ktor.client.okhttp)
    implementation(libs.ktor.client.content.negotiation)
    implementation(libs.ktor.client.logging)
    implementation(libs.ktor.serialization.kotlinx.json)

    implementation(libs.kord.core)

    implementation(libs.netty.codec.haproxy)

    implementation(libs.mockk)
}
