package com.near_reality.game.content.boss.nex.npc

import com.zenyte.game.content.godwars.GodType
import com.zenyte.game.content.godwars.npcs.AbstractKillcountNPC
import com.zenyte.game.util.Direction
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.npc.combat.CombatScript

open class BloodReaver(id: Int, tile: Location?, facing: Direction, radius: Int) :
    AbstractKillcountNPC(id, tile, facing, radius), CombatScript {

    init {
        isCrawling = true
        setInGodwars(true)
    }

    override fun attack(target: Entity): Int {
        setAnimation(Animation(9194))
        val projectile = Projectile(372, 0, 0, 51, 23, -5, 64, 10)
        delayHit(this, World.sendProjectile(this, target, projectile), target, magic(target, 20).onLand {
            target.graphics = Graphics(375, -1, 0)
            val healAmount = (it.damage * 0.25).toInt()
            if (healAmount > 0)
                heal(healAmount)
        })
        return getCombatDefinitions().attackSpeed
    }

    override fun type(): GodType = GodType.ANCIENT

}
