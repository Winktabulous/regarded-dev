package com.near_reality.game.content.araxxor

import com.near_reality.game.content.araxxor.rewards.Reward
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.ARAXXOR_CORPSE
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.dialogue.Dialogue
import com.zenyte.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-20
 */
class AraxxorCorpse: NPCPlugin() {

    override fun handle() {
        bind("Harvest") { player, corpse ->
            run {
                if (corpse.temporaryAttributes["harvesting"] != null) return@run
                corpse.temporaryAttributes["harvesting"] = true
                player.animation = Animation.GRAB
                schedule(2) {
                    val rewards = Reward().rollForItems(player)
                    if (rewards.isNotEmpty())
                        rewards.forEach { reward -> player.inventory.addOrDrop(reward) }
                    removeAraxxorCorpseAndResetRoom(player, corpse)
                }
            }
        }

        bind("Destroy") { player, corpse ->
            run {
                if (corpse.temporaryAttributes["harvesting"] != null) return@run
                player.dialogue {
                    options("This will forfeit the potential loot that comes with harvesting?",
                        Dialogue.DialogueOption("Yes, I'm sure.") {
                            corpse.temporaryAttributes["harvesting"] = true
                            player.animation = Animation.GRAB
                            schedule(2) {
                                Reward().rollForNid(player, 1/1500.0)
                                removeAraxxorCorpseAndResetRoom(player, corpse)
                            }
                        },
                        Dialogue.DialogueOption("Nevermind.")
                    )
                }
            }
        }
    }

    private fun removeAraxxorCorpseAndResetRoom(player: Player, corpse: NPC) {
        // TODO: Animation
        // TODO: Sound
        schedule(2) {
            player.mapInstance ?: return@schedule
            corpse.remove()
            corpse.temporaryAttributes.remove("harvesting")
        }
        schedule(21) {
            player.mapInstance ?: return@schedule
            val araxxorInstance = player.mapInstance as AraxxorInstance
            araxxorInstance.araxxor.remove()
            araxxorInstance.araxxor.resetInstance()
            araxxorInstance.spawnAraxxorAndEggs()
        }
    }

    override fun getNPCs(): IntArray = intArrayOf(ARAXXOR_CORPSE)
}