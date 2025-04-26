package com.near_reality.game.content.dt2.npc.whisperer.head

import com.near_reality.game.content.dt2.area.WhispererInstance
import com.near_reality.game.content.dt2.npc.DT2BossDifficulty
import com.near_reality.game.content.dt2.npc.whisperer.head.tasks.DisturbOddFigureTask
import com.near_reality.game.content.dt2.npc.whisperer.head.tasks.KnockBackTask
import com.zenyte.game.item.ItemId.BLACKSTONE_FRAGMENT_28357
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.npc.NpcId.ODD_FIGURE
import com.zenyte.game.world.entity.npc.actions.NPCPlugin
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-23
 */
class OddFigure : NPCPlugin() {

    override fun handle() {
        bind("Disturb") { player: Player, npc: NPC ->
            if (!player.inventory.containsItem(BLACKSTONE_FRAGMENT_28357)) {
                player.sendMessage("You need a Blackstone Fragment to begin this Fight.")
                return@bind
            }
            if (npc.isLocked) return@bind
            npc.lock()
            player.animation = Animation(3335)
            player.lock()
            schedule(KnockBackTask(player, npc), 0, 0)
            schedule(DisturbOddFigureTask(player, npc), 0, 0)
            // TODO: Create Instance, then the below
            WhispererInstance(DT2BossDifficulty.NORMAL, player).constructRegion()
        }
    }

    override fun getNPCs(): IntArray {
        return intArrayOf(ODD_FIGURE)
    }
}