package com.near_reality.game.content.dt2.npc.whisperer.attacks.special.impl.siphon

import com.near_reality.game.content.*
import com.near_reality.game.content.dt2.npc.whisperer.WhispererCombat
import com.near_reality.game.content.dt2.npc.whisperer.attacks.special.WhispererSpecial
import com.zenyte.game.task.WorldTasksManager.schedule
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.entity.npc.NpcId.*
import com.zenyte.game.world.entity.player.Player

/**
 * @author Glabay | Glabay-Studios
 * @project near-reality-server
 * @social Discord: Glabay
 * @since 2024-09-24
 */
object SoulSiphonSpecialAttack : WhispererSpecial {

    val lostSouls = mutableListOf<LostSoul>()

    private val offsets = arrayOf(
        -4 to 2,
        -4 to 0,
        -4 to -2,

        1 to 5,
        0 to 4,
        -1 to 5,

        4 to 2,
        4 to 0,
        4 to -2,

        1 to -5,
        0 to -4,
        -1 to -5
    )

    private fun spawnSouls(whisperer: WhispererCombat) {
        // West side
        lostSouls.add(LostSoul(whisperer, false, LostSoulType.MORS, Pair(-4, 2))) // Mors! (Green)
        lostSouls.add(LostSoul(whisperer, false, LostSoulType.MORS, Pair(-4, 0))) // Mors! (Green)
        lostSouls.add(LostSoul(whisperer, false, LostSoulType.ORATIO, Pair(-4, -2))) // Oratio! (Blue)
        // North side
        lostSouls.add(LostSoul(whisperer, false, LostSoulType.SANITAS, Pair(1, 5))) // Sanitas! (Cyan)
        lostSouls.add(LostSoul(whisperer, false, LostSoulType.ORATIO, Pair(0, 4))) // Oratio! (Blue)
        lostSouls.add(LostSoul(whisperer, false, LostSoulType.MORS, Pair(-1, 5))) // Mors! (Green)
        // East side
        lostSouls.add(LostSoul(whisperer, false, LostSoulType.SANITAS, Pair(4, 2))) // Sanitas! (Cyan)
        lostSouls.add(LostSoul(whisperer, false, LostSoulType.VITA, Pair(4, 0))) // Vita! (Yellow)
        lostSouls.add(LostSoul(whisperer, false, LostSoulType.SANITAS, Pair(4, -2))) // Sanitas! (Cyan)
        // South side
        lostSouls.add(LostSoul(whisperer, false, LostSoulType.MORS,  Pair(1, -5))) // Mors! (Green)
        lostSouls.add(LostSoul(whisperer, false, LostSoulType.VITA, Pair(0, -4))) // Vita! (Yellow)
        lostSouls.add(LostSoul(whisperer, false, LostSoulType.ORATIO, Pair(-1, -5))) // Oratio! (Blue)

        lostSouls.forEach { it.spawn() }
    }

    override fun setup(whisperer: WhispererCombat, player: Player) {
        whisperer.radius = 0
        whisperer.setLocation(whisperer.spawnLocation)
        schedule(2) { whisperer faceDir North }
    }

    override fun execute(whisperer: WhispererCombat, target: Player) {
        val projectile = Projectile(2457, 96, 0)
        // GFX for throwing orbs to the locations
        offsets.map {
            whisperer fire projectile at (whisperer.middleLocation offset it)
        }
        schedule(2) { spawnSouls(whisperer) }
    }

}