package com.near_reality.tools.discord.staff.player_search

import com.near_reality.tools.HardwareInfoModule
import com.near_reality.tools.WealthScanner
import com.near_reality.tools.discord.DiscordServer
import com.near_reality.tools.discord.includeUsernameOption
import com.near_reality.tools.discord.readUsername
import com.near_reality.tools.discord.staff.DiscordStaffBot
import com.zenyte.plugins.events.ServerLaunchEvent
import dev.kord.core.behavior.channel.TextChannelBehavior
import dev.kord.core.behavior.interaction.respondPublic
import dev.kord.core.behavior.interaction.response.createPublicFollowup
import dev.kord.rest.builder.message.embed

fun DiscordStaffBot.configureHardwareInfoSearchCommand() {
    launchGated {
        registerChatInputCommand("search_player_hardware", "Search accounts linked to all hardware_info associated with the player",
            builder = {
                includeUsernameOption()
            },
            handler = {
                val username = readUsername()
                val hardwareInfoSet = HardwareInfoModule[username]
                val hardwareInfoByUsernames =
                    hardwareInfoSet.associateWith(HardwareInfoModule::getUsernamesWithHardware)

                val channel = channel
                if (channel !is TextChannelBehavior)
                    return@registerChatInputCommand

                val response = respondPublic {
                    content = "Found `${hardwareInfoSet.size}` hardware-info configurations associated with **${username}**."
                }
                for ((info, usernames) in hardwareInfoByUsernames) {
                    response.createPublicFollowup {
                        embed {
                            title = "${info.hashCode()}"
                            description = "**Usernames:** ${usernames.take(15).joinToString()}"
                            footer {
                                text = "Found ${usernames.size} accounts with this hardware-info, " +
                                        "please note that depending on how much info is known, " +
                                        "it may not be an accurate identifier for player accounts."
                            }
                            field("cpu") {
                                "**name:** ${info.cpuName} (${info.clockSpeed}GHz, ${info.logicalProcessors}-core), " +
                                        "**model:** ${info.cpuModel}, " +
                                        "${info.cpuBrandType}, " +
                                        "${info.cpuManufacture}, " +
                                        "${info.cpuFeatures.contentToString()}, " +
                                        "${info.cpuCount}"
                            }
                            field("gpu") {
                                "**name:**${info.graphicCardName}, " +
                                        "${info.graphicCardManufacture}, " +
                                        "${info.graphicCardReleaseYear}-${info.graphicCardReleaseMonth}, " +
                                        "**dx:** ${info.dxVersion}"
                            }
                            field("mem") {
                                "**RAM:** ${info.physicalMemory}, " +
                                        "**heap:** ${info.heap}"
                            }
                            field("java") {
                                "**vendor:** ${info.javaVendorId}, " +
                                        "**version:** ${info.javaVersionMajor}." +
                                        "${info.javaVersionMinor}." +
                                        "${info.javaVersionUpdate}, " +
                                        "**applet:** ${info.isApplet}"
                            }
                            field("os") {
                                "**type:** ${info.osId}, **version:** ${info.osVersion} (Arch-x64: ${info.isArch64Bit})"
                            }
                        }
                    }
                }
            }
        )
    }
}

fun main() {
    WealthScanner.loadDefault()
    DiscordStaffBot.init(
        DiscordServer.Staff,
        token = "OTk4MzUxNjM5ODU1NTA5NTg2.GB7otV.kqeSjIbP_ecjc__G2o7KDeLBboxUuDDmiYb2yE"
    )
    HardwareInfoModule.onServerLaunched(ServerLaunchEvent(null))
    while (true) {
        Thread.sleep(1000L)
    }
}
