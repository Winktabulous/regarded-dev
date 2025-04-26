package com.near_reality.game.content.tormented_demon.items

import com.zenyte.game.item.ItemId.SMOULDERING_PILE_OF_FLESH
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.SkillConstants
import com.zenyte.game.world.flooritem.FloorItem
import com.zenyte.plugins.flooritem.FloorItemPlugin
import kotlin.math.floor

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-10-19
 */
class SmoulderingPileOfFlesh : FloorItemPlugin {

    override fun getItems(): IntArray =
        intArrayOf(SMOULDERING_PILE_OF_FLESH)

    var chunksInPile = 4

    override fun handle(player: Player?, item: FloorItem?, optionId: Int, option: String?) {
        if (player == null || item == null) return
        val heal: Int = healedAmount(player)
        val hp = player.skills.getLevelForXp(SkillConstants.HITPOINTS)
        val currentHealth = player.skills.getLevel(SkillConstants.HITPOINTS)
        val boost =
            if (hp < 10) 0
            else if (hp < 20) 3
            else if (hp < 25) 4
            else if (hp < 30) 6
            else if (hp < 40) 7
            else if (hp < 50) 8
            else if (hp < 60) 11
            else if (hp < 70) 12
            else if (hp < 75) 13
            else if (hp < 80) 15
            else if (hp < 90) 16
            else if (hp < 92) 17
            else 18
        val max = hp + boost
        if (currentHealth > max + heal) {
            return
        }
        player.setHitpoints(if ((currentHealth + heal) >= max) max else (currentHealth + heal))
        player.sendMessage("Even as the flesh burns your throat, you feel your wounds begin to mend.")
        chunksInPile--

        if (chunksInPile <= 0)
            World.destroyFloorItem(item)
    }

    private fun healedAmount(player: Player): Int {
        val hitpoints = player.skills.getLevelForXp(SkillConstants.HITPOINTS)
        val c =
            if (hitpoints < 25) 2
            else if (hitpoints < 50) 4
            else if (hitpoints < 75) 6
            else if (hitpoints < 93) 8
            else 13
        return floor((hitpoints / 10f).toDouble()).toInt() + c
    }
}