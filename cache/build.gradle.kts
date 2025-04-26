import java.net.URI
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.io.path.absolutePathString

plugins {
    id("near-reality-project-kotlin")
}

group = "com.near_reality"
version = "0.1.0"

dependencies {
    api(projects.util)

    implementation(libs.jackson.dataformat.toml)

    // START - for jire to change
    api(projects.coreModel)
    // END - for jire to change

    implementation(projects.util)
    implementation(libs.kryo)
    implementation(libs.google.guava)
    implementation(libs.apache.commons.io)
    implementation(libs.zip4j)
    implementation(libs.apache.commons.compress)
    implementation(libs.apache.commons.lang3)
    implementation(libs.jsoup)
    implementation(libs.tradukisto)
    implementation(libs.apache.ant)
    implementation(libs.jackson.module.kotlin)
    implementation(libs.apache.commons.cli)

    implementation(projects.cs2)

    api(libs.toml4j)

    api(libs.runelite.api)
    api(libs.runelite.cache)
}

val cache225Name = "cache-225.zip"
val cache225URL = "https://files.jire.org/nr211/cache-225.zip"

val keys225Name = "cache-225-keys.json"
val keys225URL = "https://files.jire.org/nr211/cache-225-keys.json"

val download225Cache = tasks.register("download225Cache") {
    group = "_nr_data"

    doLast {
        val dataDirectory = projectDir.toPath().resolve("data")

        val cache = dataDirectory.resolve(cache225Name)
        if (Files.notExists(cache)) {
            logger.lifecycle("Cache (225) does not exist. Downloading...")
            Files.createDirectories(dataDirectory)
            URI.create(cache225URL)
                .toURL()
                .openStream()
                .use { inputStream ->
                    Files.copy(
                        inputStream,
                        cache,
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            logger.lifecycle("Cache (225) download complete: {}", cache.absolutePathString())
        } else {
            logger.lifecycle("Cache (225) already exists at: {}", cache.absolutePathString())
        }

        val keys = dataDirectory.resolve(keys225Name)
        if (Files.notExists(keys)) {
            logger.lifecycle("Keys (225) does not exist. Downloading...")
            Files.createDirectories(dataDirectory)
            URI.create(keys225URL)
                .toURL()
                .openStream()
                .use { inputStream ->
                    Files.copy(
                        inputStream,
                        keys,
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            logger.lifecycle("Keys (225) download complete: {}", keys.absolutePathString())
        } else {
            logger.lifecycle("Keys (225) already exists at: {}", keys.absolutePathString())
        }
    }
}

val cache219Name = "cache-219.zip"
val cache219URL = "https://files.jire.org/nr211/cache-219.zip"

val download219Cache = tasks.register("download219Cache") {
    group = "_nr_data"

    doLast {
        val dataDirectory = projectDir.toPath().resolve("data")

        val cache = dataDirectory.resolve(cache219Name)
        if (Files.notExists(cache)) {
            logger.lifecycle("Cache (219) does not exist. Downloading...")
            Files.createDirectories(dataDirectory)
            URI.create(cache219URL)
                .toURL()
                .openStream()
                .use { inputStream ->
                    Files.copy(
                        inputStream,
                        cache,
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            logger.lifecycle("Cache (219) download complete: {}", cache.absolutePathString())
        } else {
            logger.lifecycle("Cache (219) already exists at: {}", cache.absolutePathString())
        }
    }
}

val cacheRuneSpawnName = "cache-runespawn.zip"
val cacheRuneSpawnURL = "https://files.jire.org/nr211/cache-runespawn.zip"

val downloadRuneSpawnCache = tasks.register("downloadRuneSpawnCache") {
    group = "_nr_data"

    doLast {
        val dataDirectory = projectDir.toPath().resolve("data")

        val cache = dataDirectory.resolve(cacheRuneSpawnName)
        if (Files.notExists(cache)) {
            logger.lifecycle("Cache (RuneSpawn) does not exist. Downloading...")
            Files.createDirectories(dataDirectory)
            URI.create(cacheRuneSpawnURL)
                .toURL()
                .openStream()
                .use { inputStream ->
                    Files.copy(
                        inputStream,
                        cache,
                        StandardCopyOption.REPLACE_EXISTING
                    )
                }
            logger.lifecycle("Cache (RuneSpawn) download complete: {}", cache.absolutePathString())
        } else {
            logger.lifecycle("Cache (RuneSpawn) already exists at: {}", cache.absolutePathString())
        }
    }
}

tasks.named("build") {
    dependsOn(download225Cache, download219Cache, downloadRuneSpawnCache)
}
