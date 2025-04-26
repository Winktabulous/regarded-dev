package com.near_reality

import com.near_reality.util.capitalize
import java.io.File

object Convert {

    fun String.toUpperCamelCase(): String {
        return this.split(" ", "-", "_") // Split by common delimiters
            .filter { it.isNotEmpty() }  // Remove empty parts
            .joinToString("") { it.lowercase().replaceFirstChar(Char::uppercase) } // Capitalize each word
    }

    @JvmStatic
    fun main(args: Array<String>) {
        val imports = listOf(
            "com.near_reality.scripts.npc.drops.NPCDropTableScript",

            "com.zenyte.game.world.entity.npc.NpcId",
            "com.zenyte.game.world.entity.npc.NpcId.*",

            "com.zenyte.game.item.ItemId",
            "com.zenyte.game.item.ItemId.*",
            "com.near_reality.scripts.npc.drops.table.DropTableType.*",

            "com.zenyte.game.world.entity.npc.drop.matrix.*",
            "com.zenyte.game.world.entity.npc.drop.matrix.Drop.*",
            "com.zenyte.game.world.entity.npc.drop.matrix.DropProcessor.*",
        )
        val dir = File("/home/jire/IdeaProjects/jacmob/near-reality/server-production/")
        dir.walk().forEach { file ->
            if (file.extension != "kts") return@forEach
            val name = file.name
            if (!name.endsWith(".droptable.kts")) return@forEach
            val fileName = file.nameWithoutExtension
            val className = fileName
                .toUpperCamelCase()
                .replace("\'", "")
                .replace("_", "")
                .replace("-", "")
                .replace("(", "")
                .replace(")", "")
                .replace(",", "")
                .replace("&", "")
                .replace("!", "")
                .replace(" ", "")
                .replace(".droptable", "DropTable")
                .capitalize()
            val newFileName = "$className.kt"
            val lines = file.readLines()
            val initLine = lines.indexOfFirst { it.isNotBlank() && !it.startsWith("package") && !it.startsWith("import") }
            if (initLine == -1) {
                println("Could not find init line in $fileName")
                //file.delete()
                return@forEach
            }
            val newLines = mutableListOf<String>()
            newLines.addAll(lines.subList(0, initLine))
            for (import in imports) {
                newLines.add("import $import")
            }
            newLines.add("")
            newLines.add("class $className : NPCDropTableScript() {")
            newLines.add("    init {")
            newLines.addAll(lines.subList(initLine, lines.size).map { "        $it" })
            newLines.add("    }")
            newLines.add("}")
            val joinedNewLines = newLines.joinToString("\n")
            println("writing $newFileName")
            //println(joinedNewLines)
            file.resolveSibling(newFileName).writeText(joinedNewLines, Charsets.UTF_8)
            file.delete()
            //exitProcess(1)
        }
    }

}