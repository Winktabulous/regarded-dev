package com.near_reality.plugins.item.customs

import com.near_reality.scripts.item.actions.ItemActionScript
import com.zenyte.game.item.ItemId
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.player.container.RequestResult
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import kotlin.time.Duration.Companion.hours
import kotlin.time.Duration.Companion.milliseconds

class SirEldricsBoostScrollItemAction : ItemActionScript() {
    init {
        items(ItemId.SIR_ELDRICS_BOOST_SCROLL)

        "Activate" {

            player.dialogue(NpcId.GHOST_3516) {
                if(player.variables.pvmArenaBoosterTick > 0) {
                    npc("Sir Eldric", "Ah, a blessed soul approaches!<br> There are ${Colour.RS_PURPLE.wrap((player.variables.pvmArenaBoosterTick * 600).milliseconds.inWholeMinutes.toString())} minutes of my combat boost left for thee.")
                } else {
                    npc(
                        "Sir Eldric",
                        "Activating will boost your combat against monsters by 5% for 1 hour.<br> Are you sure you want to activate it?"
                    )
                    options("Activate the scroll?") {
                        dialogueOption("Yes") {
                            val itemAtSlot = container.get(slotID)
                            if (itemAtSlot.id == ItemId.SIR_ELDRICS_BOOST_SCROLL
                                && container.remove(slotID, 1).result == RequestResult.SUCCESS
                            ) {
                                container.isFullUpdate = true
                                container.refresh(slotID)
                                container.refresh(player)
                                player.variables.pvmArenaBoosterTick = (1.hours.inWholeMilliseconds / 600).toInt()
                                player.sendMessage(Colour.RS_PURPLE.wrap("Sir Eldric boosted your combat against monsters by 5% for 1 hour."))
                            }
                        }
                        dialogueOption("No")
                    }
                }
            }
        }

    }
}

