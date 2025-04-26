@file:Suppress("FunctionName", "RemoveRedundantBackticks")

package com.near_reality.content.group_ironman.npc.actions

import com.near_reality.scripts.npc.actions.NPCActionScript
import com.zenyte.game.GameInterface
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.dialogue.OptionsBuilder
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options

class GroupStorageTutorNpcAction : NPCActionScript() {
    init {
        npcs(NpcId.GROUP_STORAGE_TUTOR)

        "Talk-to"(onClick = distancedRoute(distance = 2)) {
            player.dialogue(npc) {
                npc(
                    "Good day! I'm here to teach you about group " +
                            "storage, and to let you access your personal bank. You " +
                            "can call me Maggie. How may I help you?"
                )
                options("What would you like to say?") {
                    `How do I use the group storage`()
                    `I'd like to access my bank account please`()
                    `I'd like to check my PIN settings`()
                    `I'd like to collect items`()
                    `Goodbye`()
                }
            }
        }
        "Bank"(onClick = distancedRoute(distance = 2)) {
            GameInterface.BANK.open(player)
        }
    }
}

fun OptionsBuilder.`How do I use the group storage`() {
    dialogueOption("How do I use the group storage?") {
        npc(
            "You can access the group storage via the button in the " +
                    "top-left of your bank interface. Only one member of " +
                    "the group may access the storage at one time."
        )
        npc(
            "You will then be able to deposit and withdraw items " +
                    "from the group storage. You can't deposit untradeable items"
        )
        npc("When you have finished click the 'Save' button to confirm the transaction.")
        npc("Can I help you with anything else?")
        options("What would you like to say?") {
            `I'd like to access my bank account please`()
            `I'd like to check my PIN settings`()
            `I'd like to collect items`()
            `Goodbye`()
        }
    }
}

fun OptionsBuilder.`I'd like to access my bank account please`() {
    dialogueOption("I'd like to access my bank account please.") {
        npc("Can I help you with anything else?")
        options("What would you like to say?") {
            `How do I use the group storage`()
            `I'd like to check my PIN settings`()
            `I'd like to collect items`()
            `Goodbye`()
        }
    }
}

fun OptionsBuilder.`I'd like to check my PIN settings`() {
    dialogueOption("I'd like to check my PIN settings.") {
        npc("I'm sorry to say ${player.name} but Near reality currently does not support bank PINs.")
    }
}

fun OptionsBuilder.`I'd like to collect items`() {
    dialogueOption("I'd like to collect items.", action = GameInterface.GRAND_EXCHANGE_COLLECTION_BOX::open)
}

fun OptionsBuilder.`Goodbye`() {
    dialogueOption("Goodbye.") {}
}
