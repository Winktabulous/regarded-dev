package com.near_reality.tools

import java.nio.file.Paths
import kotlin.io.path.readText

fun main() {
    val type = "RELLEKKA_MARKET_GUARDS"
    val search = "MARKET_GUARD"
    val string = buildString {
        append("public static final int[] $type = new int[]{\n")
        Paths.get("./core/src/main/java/com/zenyte/game/world/entity/npc/NpcId.java")
            .readText()
            .substringAfter("{")
            .substringBeforeLast("}")
            .split("\n")
            .filter { it.contains(search) }
            .map {
                val (nameString, idString) = it.split("=")
                val id = idString.substringBefore(";").trim().toInt()
                id to "${nameString.trim()
                    .removePrefix("public static final int ")
                    .trim()}, "
            }
            .let { names ->
                val maxNameLength = names.maxOf { it.second.length }
                names
                    .sortedBy { it.first }
                    .map { it.second.padEnd(maxNameLength) }
                    .chunked(4)
                    .forEach { chunk ->
                        append('\t')
                        append(chunk.joinToString(""))
                        append('\n')
                    }
            }

        append("};")
    }
    println(string)

}
