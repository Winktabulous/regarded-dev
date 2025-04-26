rootProject.name = "near-reality-server"

enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

dependencyResolutionManagement {
    @Suppress("UnstableApiUsage")
    repositories {
        mavenCentral()
        gradlePluginPortal()
    }
}

pluginManagement {
    val kotlinVersion: String = settings.extra["kotlin.version"] as String
    plugins {
        kotlin("jvm").version(kotlinVersion)
        kotlin("multiplatform").version(kotlinVersion)
        kotlin("plugin.serialization").version(kotlinVersion)
    }
}

include("api")
include("cs2")

include(
    "threads",
    "util",
    "buffer-generator",
    "buffer",
    "crypto",
    "net",
    "cache",
    "core",
    "core-model",
    "core-restricted",
)

include(
    ":scripts:common",

    ":scripts:ground-items",

    ":scripts:interfaces",
    ":scripts:interfaces:user",

    ":scripts:item",
    ":scripts:item:actions",
    ":scripts:item:definitions",
    ":scripts:item:equip",

    ":scripts:npc",
    ":scripts:npc:actions",
    ":scripts:npc:definitions",
    ":scripts:npc:drops",
    ":scripts:npc:spawns",

    ":scripts:object",
    ":scripts:object:actions",

    ":scripts:player",
    ":scripts:player:actions",

    ":scripts:shops",
)

include(
    ":plugins:area:ferox_enclave",
    ":plugins:area:osnr_home:npc",
    ":plugins:area:osnr_home:obj",
    ":plugins:area:osnr_home",
    ":plugins:area",
    ":plugins:boss:ganodermic-beast",
    ":plugins:boss:zalcano",
    ":plugins:boss",
    ":plugins:elven",
    ":plugins:ground-items",
    ":plugins:interfaces:characterdesign",
    ":plugins:interfaces:credit-store",
    ":plugins:interfaces:death",
    ":plugins:interfaces:slayer",
    ":plugins:interfaces:teleports",
    ":plugins:interfaces",
    ":plugins:item:actions:death-items",
    ":plugins:item:actions",
    ":plugins:item:cosmetics",
    ":plugins:item:customs",
    ":plugins:item:definitions",
    ":plugins:item:equip",
    ":plugins:item:lootkey",
    ":plugins:item:staff-of-balance",
    ":plugins:item",
    ":plugins:larranskey",
    ":plugins:npc:definitions",
    ":plugins:npc:drops",
    ":plugins:npc",
    ":plugins:object",
    ":plugins:rewards",
    ":plugins:shops",
    ":plugins:spawns:custom",
    ":plugins:spawns:nex",
    ":plugins:spawns:region10xxx",
    ":plugins:spawns:region11xxx",
    ":plugins:spawns:region12xxx",
    ":plugins:spawns:region13xxx",
    ":plugins:spawns:region14xxx",
    ":plugins:spawns:region15xxx",
    ":plugins:spawns:region16xxx",
    ":plugins:spawns:region17xxx",
    ":plugins:spawns:region4xxx",
    ":plugins:spawns:region5xxx",
    ":plugins:spawns:region6xxx",
    ":plugins:spawns:region7xxx",
    ":plugins:spawns:region8xxx",
    ":plugins:spawns:region9xxx",
    ":plugins:spawns",
)

// excluded
include(
    ":plugins:excluded",
    ":plugins:excluded:area",
    ":plugins:excluded:boss",
    ":plugins:excluded:boss:abyssalsire",
    ":plugins:excluded:boss:nex",
    ":plugins:excluded:boss:nightmare",
    ":plugins:excluded:gauntlet",
    ":plugins:excluded:group-ironman",
    ":plugins:excluded:itemonitem",
    ":plugins:excluded:itemonitem:impl",
    ":plugins:excluded:itemonitem:neitiznot_faceguard",
    ":plugins:excluded:itemonobject",
    ":plugins:excluded:itemonobject:elemental_tiara",
    ":plugins:excluded:muddychest",
    ":plugins:excluded:skills",
    ":plugins:excluded:skills:agility",
    ":plugins:excluded:skills:agility:priffdinasrooftop",
    ":plugins:excluded:theatreofblood",
    ":plugins:excluded:tools",
    ":plugins:excluded:tools:analyzer",
    ":plugins:excluded:tools:backups",
    //":plugins:excluded:tools:cloudflare", <- only needed when interacting with cloudflare api
    ":plugins:excluded:tools:discord",
    ":plugins:excluded:tools:updater",
)

include("app")
