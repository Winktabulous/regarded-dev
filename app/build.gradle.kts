plugins {
    id("near-reality-project-kotlin")
    application
    alias(libs.plugins.shadow)
}

group = "com.near_reality"
version = "0.1.0"

dependencies {
    runtimeOnly(libs.slf4j.api)
    runtimeOnly(libs.logback.classic)

    runtimeOnly(projects.core)
    runtimeOnly(projects.coreRestricted)

    runtimeOnly(projects.plugins.area.feroxEnclave)
    runtimeOnly(projects.plugins.area.osnrHome.npc)
    runtimeOnly(projects.plugins.area.osnrHome.obj)
    runtimeOnly(projects.plugins.area.osnrHome)
    runtimeOnly(projects.plugins.area)
    runtimeOnly(projects.plugins.boss.ganodermicBeast)
    runtimeOnly(projects.plugins.boss.zalcano)
    runtimeOnly(projects.plugins.boss)
    runtimeOnly(projects.plugins.elven)
    runtimeOnly(projects.plugins.groundItems)
    runtimeOnly(projects.plugins.interfaces.characterdesign)
    runtimeOnly(projects.plugins.interfaces.death)
    runtimeOnly(projects.plugins.interfaces.slayer)
    runtimeOnly(projects.plugins.interfaces.teleports)
    runtimeOnly(projects.plugins.interfaces)
    runtimeOnly(projects.plugins.item.actions.deathItems)
    runtimeOnly(projects.plugins.item.actions)
    runtimeOnly(projects.plugins.item.cosmetics)
    runtimeOnly(projects.plugins.item.customs)
    runtimeOnly(projects.plugins.item.definitions)
    runtimeOnly(projects.plugins.item.equip)
    runtimeOnly(projects.plugins.item.lootkey)
    runtimeOnly(projects.plugins.item.staffOfBalance)
    runtimeOnly(projects.plugins.item)
    runtimeOnly(projects.plugins.larranskey)
    runtimeOnly(projects.plugins.npc.definitions)
    runtimeOnly(projects.plugins.npc.drops)
    runtimeOnly(projects.plugins.npc)
    runtimeOnly(projects.plugins.`object`)
    runtimeOnly(projects.plugins.rewards)
    runtimeOnly(projects.plugins.shops)
    runtimeOnly(projects.plugins.spawns.custom)
    runtimeOnly(projects.plugins.spawns.nex)
    runtimeOnly(projects.plugins.spawns.region10xxx)
    runtimeOnly(projects.plugins.spawns.region11xxx)
    runtimeOnly(projects.plugins.spawns.region12xxx)
    runtimeOnly(projects.plugins.spawns.region13xxx)
    runtimeOnly(projects.plugins.spawns.region14xxx)
    runtimeOnly(projects.plugins.spawns.region15xxx)
    runtimeOnly(projects.plugins.spawns.region16xxx)
    runtimeOnly(projects.plugins.spawns.region17xxx)
    runtimeOnly(projects.plugins.spawns.region4xxx)
    runtimeOnly(projects.plugins.spawns.region5xxx)
    runtimeOnly(projects.plugins.spawns.region6xxx)
    runtimeOnly(projects.plugins.spawns.region7xxx)
    runtimeOnly(projects.plugins.spawns.region8xxx)
    runtimeOnly(projects.plugins.spawns.region9xxx)
    runtimeOnly(projects.plugins.spawns)

    // excluded
    runtimeOnly(projects.plugins.excluded)
    runtimeOnly(projects.plugins.excluded.area)
    runtimeOnly(projects.plugins.excluded.boss)
    runtimeOnly(projects.plugins.excluded.boss.abyssalsire)
    runtimeOnly(projects.plugins.excluded.boss.nex)
    runtimeOnly(projects.plugins.excluded.boss.nightmare)
    runtimeOnly(projects.plugins.excluded.gauntlet)
    runtimeOnly(projects.plugins.excluded.groupIronman)
    runtimeOnly(projects.plugins.excluded.itemonitem)
    runtimeOnly(projects.plugins.excluded.itemonitem.impl)
    runtimeOnly(projects.plugins.excluded.itemonitem.neitiznotFaceguard)
    runtimeOnly(projects.plugins.excluded.itemonobject)
    runtimeOnly(projects.plugins.excluded.itemonobject.elementalTiara)
    runtimeOnly(projects.plugins.excluded.muddychest)
    runtimeOnly(projects.plugins.excluded.skills)
    runtimeOnly(projects.plugins.excluded.skills.agility)
    runtimeOnly(projects.plugins.excluded.skills.agility.priffdinasrooftop)
    runtimeOnly(projects.plugins.excluded.theatreofblood)
    runtimeOnly(projects.plugins.excluded.tools)
    runtimeOnly(projects.plugins.excluded.tools.analyzer)
    runtimeOnly(projects.plugins.excluded.tools.backups)
    runtimeOnly(projects.plugins.excluded.tools.discord)
    runtimeOnly(projects.plugins.excluded.tools.updater)

    for (classifier in arrayOf(
        "windows-x86_64",

        "linux-x86_64",
        "linux-aarch_64",

        "osx-x86_64",
        "osx-aarch_64",
    )) {
        runtimeOnly(variantOf(libs.netty.tcnative.boringssl.static) {
            classifier(classifier)
        })
    }

    for (classifier in arrayOf(
        "linux-x86_64",
        "linux-aarch_64",
    )) {
        runtimeOnly(variantOf(libs.netty.transport.native.epoll) {
            classifier(classifier)
        })
        runtimeOnly(variantOf(libs.netty.incubator.transport.native.iouring) {
            classifier(classifier)
        })
    }

    for (classifier in arrayOf(
        "osx-x86_64",
        "osx-aarch_64",
    )) {
        runtimeOnly(variantOf(libs.netty.transport.native.kqueue) {
            classifier(classifier)
        })
        runtimeOnly(variantOf(libs.netty.resolver.dns.native.macos) {
            classifier(classifier)
        })
    }

}

fun execTask(name: String, groupName: String, configure: (JavaExecSpec.() -> Unit)? = null) =
    tasks.register(name, JavaExec::class.java) {
        group = groupName

        classpath = sourceSets.main.get().runtimeClasspath
        mainClass.set("com.zenyte.Main")
        jvmArgs(
            "-XX:+UseZGC",
            "-XX:-OmitStackTraceInFastThrow",

            "-Dio.netty.tryReflectionSetAccessible=true", // allow Netty to use direct buffer optimizations
            "-Dio.netty.handler.ssl.openssl.useTasks=true", // enable OpenSSL native engine

            "--add-opens=java.base/java.time=ALL-UNNAMED",

            "--add-opens=java.base/sun.nio.ch=ALL-UNNAMED",
            "--add-opens=java.base/java.lang=ALL-UNNAMED",
            "--add-opens=java.base/java.lang.reflect=ALL-UNNAMED",
            "--add-opens=java.base/java.io=ALL-UNNAMED",
            "--add-opens=jdk.unsupported/sun.misc=ALL-UNNAMED",
            "--add-opens=java.base/jdk.internal.misc=ALL-UNNAMED",

            "--add-opens=java.base/java.nio=ALL-UNNAMED",
            "--add-opens=java.base/java.security=ALL-UNNAMED",
            "--add-opens=java.base/sun.security.action=ALL-UNNAMED",
            "--add-opens=jdk.naming.rmi/com.sun.jndi.rmi.registry=ALL-UNNAMED",
            "--add-opens=java.base/sun.net=ALL-UNNAMED",
        )

        enableAssertions = true
        workingDir = layout.projectDirectory.dir("../").asFile
        if (hasProperty("args")) {
            val argsProperty = property("args")
            val argsList = argsProperty as List<*>
            if (argsList.isNotEmpty()) {
                args(argsList)
            }
        }

        configure?.invoke(this)
    }

val generateCache = execTask("generateCache", "_nr_data") {
    mainClass.set("mgi.tools.parser.TypeParser")
    args = listOf("--unzip", "false")

    workingDir = file("../cache/")
}

val generateCacheProd = execTask("generateCacheProd", "_nr_data") {
    mainClass.set("mgi.tools.parser.TypeParser")
    args = listOf("--unzip", "false", "production")

    workingDir = file("../cache/")
}

val generatePerkData = execTask("generatePerkData", "_nr_data") {

    group = "application"
    mainClass.set("com.zenyte.game.content.boons.BoonDataGenerator")
    workingDir = file("../")
}

tasks.named("generateCache") {
    dependsOn(generatePerkData)
}

tasks.named("generateCacheProd") {
    dependsOn(generatePerkData)
}

val runPluginScanner = execTask("runPluginScanner", "_nr_data") {
    mainClass.set("com.zenyte.plugins.PluginScanner")
}

execTask("runDev", "_nr_dev") {
    args = listOf("localhost")
//    tryEnableSignozLogging("game-dev")
}
tasks.named("runDev") {
    dependsOn(runPluginScanner)
}

execTask("runBeta", "_nr") {
    args = listOf("beta")
//    tryEnableSignozLogging("game-beta")
}
tasks.named("runBeta") {
    dependsOn(runPluginScanner)
}

execTask("runBetaUpdate", "_nr") {
    args = listOf("beta")
//    tryEnableSignozLogging("game-beta")
}
tasks.named("runBetaUpdate") {
    dependsOn(generateCacheProd)
    dependsOn(runPluginScanner)
}

execTask("runProduction", "_nr") {
    jvmArgs("-Xmx60g", "-Xms24g", "-XX:+UseZGC")
//    tryEnableNewRelicLogging()
//    tryEnableSignozLogging("game-live")
    args = listOf("main")
}

execTask("runProductionUpdate", "_nr") {
    jvmArgs("-Xmx60g", "-Xms24g", "-XX:+UseZGC")
//    tryEnableNewRelicLogging()
//    tryEnableSignozLogging("game-live")
    args = listOf("main")
}

fun JavaExecSpec.tryEnableSignozLogging(appName: String) {
    val path = "/opt/signoz/opentelemetry-javaagent.jar"

    environment(
        "OTEL_TRACES_EXPORTER" to "console",
        "OTEL_METRICS_EXPORTER" to "console",
        "OTEL_LOGS_EXPORTER" to "otlp",
//        "OTEL_METRIC_EXPORT_INTERVAL" to 1500,
        "OTEL_LOGS_EXPORT_INTERVAL" to 15000,
        "OTEL_EXPORTER_OTLP_ENDPOINT" to "http://localhost:4318",
        "OTEL_RESOURCE_ATTRIBUTES" to "service.name=$appName"
    )
    if (file(path).exists()) {
        jvmArgs(
//            "-Dotel.traces.exporter=otlp",
//            "-Dotel.metrics.exporter=otlp",
//            "-Dotel.logs.exporter=otlp",
//            "-Dotel.exporter.otlp.traces.endpoint=http://localhost:4317",
//            "-Dotel.exporter.otlp.metrics.endpoint=http://localhost:4317",
//            "-Dotel.exporter.otlp.logs.endpoint=http://localhost:4317",
//            "-Dotel.resource.attributes=service.name=demo-service-java",
            "-Dotel.java agent.debug=true",
            "-javaagent:$path"
        )
        logger.info("Signoz agent found at $path, enabling...")
    } else {
        logger.warn("Signoz agent not found at $path, skipping...")
    }
}

fun JavaExecSpec.tryEnableNewRelicLogging() {
    if (file("/opt/newrelic/newrelic.jar").exists())
        jvmArgs("-javaagent:/opt/newrelic/newrelic.jar")
}

tasks.named("runProduction") {
    dependsOn(runPluginScanner)
}

tasks.named("runProductionUpdate") {
    dependsOn(generateCacheProd)
    dependsOn(runPluginScanner)
}

execTask("updateNPCOptions", "_nr_misc") {
    mainClass.set("com.zenyte.game.world.entity.npc.actions.NPCPlugin")
}

execTask("dumpSprites", "_nr_misc") {
    mainClass.set("com.near_reality.cache_tool.dumping.SpriteDumper")
}

application {
    applicationName = "near-reality-server"
    mainClass.set("com.zenyte.Main")
}

tasks.shadowJar {
    archiveBaseName.set("shadow")
    archiveClassifier.set("")
    archiveVersion.set("")

    isZip64 = true
}

execTask("scanLogs", "_nr_misc") {
    mainClass.set("com.near_reality.util.ScanLogs")
}

execTask("runDatabaseRunner", "_nr_dev") {
    mainClass.set("com.zenyte.DatabaseRunner")
}

execTask("wealthScanner", "_nr_misc") {
    mainClass.set("com.near_reality..tools.WealthScanner")
}

execTask("ecoSearch", "_nr_misc") {
    mainClass.set("com.near_reality.tools.EcoSearch")
}

execTask("totalEcoSearch", "_nr_misc") {
    mainClass.set("com.near_reality.tools.TotalEcoSearch")
}

execTask("eco", "_nr_misc") {
    mainClass.set("com.near_reality.tools.Eco")
}
execTask("simulateDrops", "_nr_misc") {
    mainClass.set("com.near_reality.tools.DropTableSimulator")

}

tasks.named<Zip>("distZip").configure {
    enabled = false
}

tasks.named<Tar>("distTar").configure {
    enabled = false
}
