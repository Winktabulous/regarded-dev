package com.near_reality.plugins.area.osrs_home.npc

import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.ContentConstants
import com.zenyte.game.GameInterface
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

class PerkMasterNPCAction : NPCActionScript() {

    init {
        npcs(NpcId.PERK_MASTER)

        "Talk-to" {
            player.dialogue(npc) {
                options("Perk Master Options") {
                    dialogueOption("Buy Perks", true) {
                        GameInterface.PVPW_PERKS.open(player)
                    }
                    dialogueOption("Exchange Items", true) {
                        GameInterface.REMNANT_EXCHANGE.open(player)
                    }
                    dialogueOption("Item Values", true) {
                        GameInterface.PVPW_PRICES.open(player)
                    }
                    dialogueOption("Open Shop", true) {
                        player.openShop("Remnant Shop")
                    }
                    dialogueOption("What are perks?", true) {
                        npc("In " + ContentConstants.SERVER_NAME + " we have a number of helpful boosts that can be unlocked through spending remnant points on them.")
                        npc("Remnant points are earned through sacrificing powerful items in the forge to my right")
                    }

                }
            }
        }

        "Buy Perks" {
            GameInterface.PVPW_PERKS.open(player)
        }

        "Exchange Remnants" {
            GameInterface.REMNANT_EXCHANGE.open(player)
        }

        "Item Values" {
            GameInterface.PVPW_PRICES.open(player)
        }

        "Open Store" {
            player.openShop("Remnant Shop")
        }
    }

}