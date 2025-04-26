package com.near_reality.game.content

import com.near_reality.game.world.entity.player.sanityValue
import com.zenyte.game.content.skills.prayer.Prayer
import com.zenyte.game.util.Direction
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.AbstractEntity
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.attribute
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.NPC
import com.zenyte.game.world.entity.player.Player

val ProtectFromMagic = Prayer.PROTECT_FROM_MAGIC
val ProtectFromMelee = Prayer.PROTECT_FROM_MELEE
val ProtectFromMissiles = Prayer.PROTECT_FROM_MISSILES

val North = Direction.NORTH
val South = Direction.SOUTH
val East = Direction.EAST
val West = Direction.WEST
val WestEast = Direction.NORTH_EAST

infix fun Player.sanity(i: Int) = run { this.sanityValue += i }
infix fun Pair<AbstractEntity, Int>.delay(i: Int) : Graphics = Graphics(this.second, i, 0)
infix fun Graphics.send(location: Location) = run { World.sendGraphics(this, location) }
infix fun NPC.hit(target: Entity) : Hit = Hit(this, 0, HitType.DEFAULT)
infix fun Hit.damage(dmg: Int) : Hit = run { this.damage = dmg; return this }
infix fun Hit.withVenom(dmg: Int) : Hit = run { this.damage = dmg; this.hitType = HitType.VENOM; return this }

infix fun AbstractEntity.prayerActive(prayer: Prayer) : Boolean {
    if(this !is Player)
        return false
    return this.prayerManager.isActive(prayer)
}

infix fun AbstractEntity.under(entity: AbstractEntity) : Boolean = !this.collides(listOf(entity), entity.x, entity.y)
infix fun AbstractEntity.nextTo(entity: AbstractEntity) : Boolean = this.location.getAxisDistance(this.size, entity.position, entity.size) == 1
infix fun AbstractEntity.spotanim(gfx: Int) : Pair<AbstractEntity, Int> = (this to gfx)
infix fun AbstractEntity.seq(sequence: Int) = run { this.animation = Animation(sequence) }
infix fun AbstractEntity.faceDir(direction: Direction)  = this.faceDirection(direction)
infix fun AbstractEntity.fire(data: Projectile) = this to data

infix fun Pair<AbstractEntity, Projectile>.at(target: Entity) = World.sendProjectile(this.first, target, this.second)
infix fun Pair<AbstractEntity, Projectile>.at(target: Location) = World.sendProjectile(this.first, target, this.second)


infix fun Location.offset(pair: Pair<Int, Int>) : Location = this.transform(pair.first, pair.second)
