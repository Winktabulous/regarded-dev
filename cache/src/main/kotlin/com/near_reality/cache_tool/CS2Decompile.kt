package com.near_reality.cache_tool

import com.near_reality.cache.file
import com.near_reality.cache.group
import mgi.tools.jagcached.ArchiveType
import org.runestar.cs2.SCRIPT_NAMES
import org.runestar.cs2.cg.StrictGenerator
import org.runestar.cs2.decompile
import org.runestar.cs2.type.Script
import org.runestar.cs2.util.*
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.exists
import kotlin.io.path.readBytes


private const val CS2_COMPILE_EXTENSION = ".cs2.pack"

fun main() {
    cacheTo
    decompileMackStore()
//    decompileQuestTab()
}

private fun decompileMackStore() {
    val readme = StringBuilder()
    readme.append("[![Discord](https://img.shields.io/discord/384870460640329728.svg?logo=discord)](https://discord.gg/G2kxrnU)\n\n")

    val baseDir =
        Path.of("/Users/stanvanderbend/IdeaProjects/near-reality/server-production/cache/assets/osnr/store_interface/")
    val loadDir = baseDir.resolve("cs2")
    val saveDir = baseDir.resolve("cs2_decompiled")
    decompileCs2(saveDir, readme, loadDir)
}

private fun decompileQuestTab() {
    val readme = StringBuilder()
    readme.append("[![Discord](https://img.shields.io/discord/384870460640329728.svg?logo=discord)](https://discord.gg/G2kxrnU)\n\n")

    val baseDir =
        Path.of("/Users/stanvanderbend/IdeaProjects/near-reality/server-production/cache/assets/cs2/")
    val loadDir = baseDir.resolve("quest_tab")
    val saveDir = baseDir.resolve("quest_tab_decompiled")
    decompileCs2(saveDir, readme, loadDir, cs2Extension = ".cs2")
}


private fun decompileCs2(saveDir: Path, readme: StringBuilder, loadDir: Path, cs2Extension : String = CS2_COMPILE_EXTENSION) {
    Files.createDirectories(saveDir)

    val generator = StrictGenerator { scriptId, scriptName, script ->
        Files.writeString(saveDir.resolve("$scriptName.cs2"), script)
        if (SCRIPT_NAMES.load(scriptId) != null) {
            readme.append("[**$scriptId**](scripts/$scriptName.cs2) `$scriptName`  \n")
        } else {
            readme.append("[**$scriptId**](scripts/$scriptName.cs2)  \n")
        }
    }

    val scriptLoader = Loader {
        val scriptFileName = it.toString() + cs2Extension
        val scriptFile = loadDir.resolve(scriptFileName)
        if (scriptFile.exists()) {
            var scriptBytes = scriptFile.readBytes()
            println("Loading $scriptFile")
            Script(scriptBytes)
        } else
            null
    }.orElse {
        println("Loading from cache $it")
        val scriptData = cacheTo.getArchive(ArchiveType.CLIENTSCRIPTS).group(it).file(0).data.run {
            position = 0
            readFullBytes()
        }
        Script(scriptData)
    }.caching()
    val scriptIds = loadDir
        .list()
        .filter { it.endsWith(CS2_COMPILE_EXTENSION) }
//        .filter { it.startsWith("32030") }
        .mapTo(TreeSet()) {
            it.substringBefore(cs2Extension).toInt()
        }

    decompile(scriptLoader.withIds(scriptIds), generator)

    println(readme)
    Files.writeString(saveDir.resolve("README.md"), readme)
}

