package com.near_reality.game.content.araxxor.cave_hunt

import com.zenyte.game.content.follower.impl.BossPet
import com.zenyte.game.world.entity.ForceTalk
import com.zenyte.game.world.entity.npc.NpcId.ARANCINI
import com.zenyte.game.world.entity.npc.NpcId.VEFARI
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.dialogue.dialogue

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-11-16
 */
class Arancini: NPCPlugin() {
    override fun handle() {
        bind("Talk-to") { player, npc ->
            if (player.follower != null) {
                if (player.follower.pet.petId() == BossPet.NID.petId ||
                    player.follower.pet.petId() == BossPet.RAX.petId) {
                    (player.mapInstance as AraxyteCaveHunt).roomCompleted = true
                    npc.forceTalk = ForceTalk("Thank you for bringing me a friend.")
                }
            }
            else
                npc.forceTalk = ForceTalk("Lonely.")
        }
    }

    override fun getNPCs(): IntArray = intArrayOf(ARANCINI)
}