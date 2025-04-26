package org.runestar.cs2

import org.runestar.cs2.cg.StrictGenerator
import org.runestar.cs2.type.Script
import org.runestar.cs2.util.Loader
import org.runestar.cs2.util.caching
import org.runestar.cs2.util.list
import org.runestar.cs2.util.withIds
import java.nio.file.Files
import java.nio.file.Path
import java.util.*
import kotlin.io.path.readBytes

private const val CS2_COMPILE_EXTENSION = ".cs2.pack"

fun main() {
    val readme = StringBuilder()
    readme.append("[![Discord](https://img.shields.io/discord/384870460640329728.svg?logo=discord)](https://discord.gg/G2kxrnU)\n\n")

    val baseDir = Path.of("/Users/stanvanderbend/IdeaProjects/near-reality/server-production/cache/assets/osnr/store_interface/")
    val loadDir = baseDir.resolve("cs2")
    val saveDir = baseDir.resolve("cs2_decompiled")
    Files.createDirectories(saveDir)

    val generator = StrictGenerator { scriptId, scriptName, script ->
        Files.writeString(saveDir.resolve("$scriptName.cs2"), script)
        if (SCRIPT_NAMES.load(scriptId) != null) {
            readme.append("[**$scriptId**](scripts/$scriptName.cs2) `$scriptName`  \n")
        } else {
            readme.append("[**$scriptId**](scripts/$scriptName.cs2)  \n")
        }
    }

    val dependencyScriptsLoader = Loader {

    }.caching()
    val scriptLoader = Loader {
        val scriptFileName = it.toString()+CS2_COMPILE_EXTENSION
        val scriptFile = loadDir.resolve(scriptFileName)
        var scriptBytes = scriptFile.readBytes()
        println("Loading $scriptFile")
        if (scriptBytes[0] != 0.toByte()) {
//            scriptBytes = byteArrayOf(0) + scriptBytes
            println("Appending byte 0 to start of script $scriptFileName")
        }
        Script(scriptBytes)
    }.caching()
    val scriptIds = loadDir.list().mapTo(TreeSet()) { it.substringBefore(CS2_COMPILE_EXTENSION).toInt() }

    decompile(scriptLoader.withIds(scriptIds), generator)

    println(readme)
    Files.writeString(saveDir.resolve("README.md"), readme)
}
