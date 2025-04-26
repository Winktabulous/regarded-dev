package com.near_reality.util

import java.nio.file.Path

object ScanLogs {

    @JvmStatic
    fun main(args: Array<String>) {
        Path.of("data", "logs", "player logs").toFile().walkTopDown().forEach {
            if (it.extension == "log") {
                for (line in it.readLines()) {
                    if (line.contains("CommandEvent]") && line.contains("eitem", true)) {
                        println("\"${it.nameWithoutExtension}\" $line")
                    }
                }
            }
        }
    }

}