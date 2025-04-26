package com.near_reality.game.content.boss.nex.`object`.actions

import com.near_reality.game.content.boss.nex.NexModule
import com.near_reality.game.content.boss.nex.nexGodwarsInstance
import com.near_reality.scripts.`object`.actions.ObjectActionScript
import com.zenyte.game.util.Utils
import com.zenyte.game.world.World
import com.zenyte.game.world.WorldThread
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.player.Bonuses
import com.zenyte.game.world.`object`.ObjectId

class StalagmiteObjectAction : ObjectActionScript() {
    init {
        ObjectId.STALAGMITE_42944 {
            when(option) {
                "Attack" -> {
                    if (player.nexGodwarsInstance == null && !NexModule.isNexSpawned() || player.nexGodwarsInstance != null && !player.nexGodwarsInstance!!.isNexSpawned()) {
                        player.sendMessage("Nothing interesting happens.")
                        return@STALAGMITE_42944
                    }

                    if (!player.combatDefinitions.attackStyle.type.isMelee) {
                        player.sendMessage("You can only break stalagmites with a melee attack.")
                        return@STALAGMITE_42944
                    }

                    val ticks: Int = player.getNumericTemporaryAttributeOrDefault("stalagmite_break", -1).toInt()
                    if (ticks > WorldThread.getCurrentCycle()) {
                        return@STALAGMITE_42944
                    }

                    player.addTemporaryAttribute("stalagmite_break", WorldThread.getCurrentCycle() + 1)
                    val bonus: Int = player.bonuses.getBonus(Bonuses.Bonus.ATT_CRUSH)
                    player.faceObject(obj)
                    player.animation = Animation(player.equipment.getAttackAnimation(player.combatDefinitions.style))
                    val chance: Int = Utils.interpolate(25, 100, 0, 130, 0.coerceAtLeast(bonus))
                    if (chance >= Utils.random(100)) {
                        if (player.nexGodwarsInstance != null)
                            player.nexGodwarsInstance!!.getNex()?.breakIcePrison()
                        else
                            NexModule.getNexNPC()?.breakIcePrison()
                        World.removeObject(obj)
                    }
                }
            }
        }

    }
}
