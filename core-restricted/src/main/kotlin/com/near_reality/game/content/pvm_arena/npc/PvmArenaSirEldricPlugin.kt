package com.near_reality.game.content.pvm_arena.npc

import com.near_reality.game.world.entity.player.pvmArenaPoints
import com.zenyte.game.util.Colour
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue
import com.zenyte.game.world.entity.player.dialogue.options
import kotlin.time.Duration.Companion.milliseconds

/**
 * Handles the PvM Arena Ghost NPC.
 *
 * @author Stan van der Bend
 */
@Suppress("unused", "SpellCheckingInspection")
class PvmArenaSirEldricPlugin : NPCPlugin() {

    override fun handle() {
        bind("Talk-To") { player, npc ->
            player.dialogue(npc) {
                if (player.variables.pvmArenaBoosterTick > 0) {
                    npc(
                        "Sir Eldric",
                        "Ah, a blessed soul approaches!<br>" +
                                "There are ${Colour.RS_PURPLE.wrap((player.variables.pvmArenaBoosterTick * 600).milliseconds.inWholeMinutes.toString())} minutes of my combat boost left for thee."
                    )
                } else {
                    npc(
                        "Sir Eldric",
                        "Ah, a brave soul approaches!<br>" +
                                "Welcome, traveler, to the remnants of the once-great arena where knights and warriors tested their mettle against fearsome foes.<br>"
                    )
                }
                openOptionsMenu(player, npc)
            }
        }
        bind("Trade") { player, _ ->
            player.openShop("PVM Arena Shop")
            player.sendMessage("You currently have ${Colour.RS_RED.wrap(player.pvmArenaPoints.toString())} PvM Arena Points.")
        }
    }

    private fun Dialogue.openOptionsMenu(player: Player, npc: NPC) {
        options {
            "What is this place?" {
                player.dialogue(npc) {
                    npc(
                        "Sir Eldric",
                        "This hallowed ground, now known as the PvM Arena,<br>" +
                                "is where teams of courageous adventurers clash with a series of formidable bosses in a trial of strength and strategy."
                    )
                    npc(
                        "Sir Eldric",
                        "Each team, armed with their own gear and supplies,<br>" +
                                "will face the same foes in sequence.<br>" +
                                "Coordination and valor are key, for the first team to slay all the monstrosities will be declared the victors."
                    )
                    npc(
                        "Sir Eldric",
                        "Triumph brings not only honor but also Arena Points,<br>" +
                                "which can be exchanged for valuable rewards and a potent blessing, a damage boost against monsters for a short time."
                    )
                    openOptionsMenu(player, npc)
                }
            }
            "What can I do with PVM Arena Points?" {
                player.dialogue(npc) {
                    npc(
                        "Sir Eldric",
                        "The spirits of vanquished foes yield Arena Points, a token of their strength.<br>" +
                                "Through me, you can redeem these points for powerful artifacts and supplies that aid in your quests.<br>"

                    )
                    npc(
                        "Sir Eldric",
                        "The rewards are many, each designed to prepare you for even greater challenges."
                    )
                    openOptionsMenu(player, npc)
                }
            }
            "How do I play?" {
                player.dialogue(npc) {
                    npc(
                        "Sir Eldric",
                        "To enter the fray of the PvM Arena,<br>" +
                                "you must choose your allegiance by stepping into one of two portals here in the arena."
                    )
                    npc(
                        "Sir Eldric",
                                "Each portal teleports you to an area designated for one of the two teams. " +
                            "Prepare well, for once entered, the battle begins posthaste."
                    )
                    npc(
                        "Sir Eldric",
                        "Gather your allies, arm yourselves, and step forth into the portal when you are ready to test your valor."
                    )
                    openOptionsMenu(player, npc)
                }
            }
            "Who are you?" {
                player.dialogue(npc) {
                    npc(
                        "Sir Eldric",
                        "I am Sir Eldric the Bound,<br>" +
                                "once a knight of great renown and now an eternal guardian of this spectral arena."

                    )
                    npc(
                        "Sir Eldric",
                        "In life, I defended the realm's most sacred treasures against dark forces, " +
                                "a quest that ultimately led to my current fate.<br>"
                    )
                    npc(
                        "Sir Eldric",
                        "Bound by an ancient curse during the final battle against the sorcerer Malverath,<br>" +
                                "my spirit was tethered to these ruins."
                    )
                    npc(
                        "Sir Eldric",
                        "Now, I serve as a guide and overseer to those who seek to test their skills in the hallowed trials<br>" +
                                "of the PvM Arena."
                    )
                    openOptionsMenu(player, npc)
                }
            }
        }
    }

    override fun getNPCs(): IntArray =
        intArrayOf(NpcId.GHOST_3516)
}
