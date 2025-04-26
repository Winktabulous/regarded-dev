package com.zenyte.game.content.chambersofxeric

import com.zenyte.GameToggles
import com.zenyte.game.item.Item
import com.zenyte.game.world.entity.player.GameCommands.Command
import com.zenyte.game.world.entity.player.privilege.PlayerPrivilege

object ChambersCommands {
    fun register() {
        Command(PlayerPrivilege.DEVELOPER, "togglecoxmasses") { player, _ ->
            GameToggles.COX_MASSES_ENABLED = !GameToggles.COX_MASSES_ENABLED
            player.sendMessage("CoX Masses Enabled: ${GameToggles.COX_MASSES_ENABLED}")
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "coxscale") { p, args ->
            if(!GameToggles.COX_MASSES_ENABLED) {
                p.sendMessage("CoX Masses are currently disabled. Please ask a manager to enable them.")
                return@Command
            }
            val raid = p.raid
            if(raid.isPresent && args.isNotEmpty()) {
                val scale = args[0].toIntOrNull()
                if (scale == null) {
                    p.sendMessage("Invalid scale, please use a number.")
                    return@Command
                }
                raid.get().setScale(scale)
                p.sendMessage("Set the difficulty scale of your raid party to $scale")
            }
        }

        Command(PlayerPrivilege.ADMINISTRATOR, "coxsupplies") { p, _ ->
            if(!GameToggles.COX_MASSES_ENABLED) {
                p.sendMessage("CoX Masses are currently disabled. Please ask a manager to enable them.")
                return@Command
            }
            val raid = p.raid
            if(raid.isPresent) {
                raid.get().constructOrGetSharedStorage().depositFromGod(Item(20996, 250), true)
                raid.get().constructOrGetSharedStorage().depositFromGod(Item(20972, 250), true)
                raid.get().constructOrGetSharedStorage().depositFromGod(Item(20984, 250), true)
                raid.get().constructOrGetSharedStorage().depositFromGod(Item(20960, 250), true)
                p.sendMessage("You have filled your team's storage with gifts from Kryeus")
            }

        }


    }
}
