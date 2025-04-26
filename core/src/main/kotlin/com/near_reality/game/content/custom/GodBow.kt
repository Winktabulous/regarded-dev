package com.near_reality.game.content.custom

import com.near_reality.game.content.crystal.CrystalRecipe
import com.near_reality.game.content.crystal.recipes.CrystalCorrupted
import com.near_reality.game.content.crystal.recipes.chargeable.CrystalWeapon
import com.near_reality.game.item.CustomItemId
import com.near_reality.game.world.entity.player.action.combat.AmmunitionDefinition
import com.near_reality.game.world.entity.player.action.combat.ISpecialAttack
import com.near_reality.game.world.entity.player.action.combat.ranged.GodBowCombat
import com.zenyte.game.content.godwars.npcs.KreeArra
import com.zenyte.game.task.WorldTasksManager
import com.zenyte.game.util.Utils
import com.zenyte.game.world.Projectile
import com.zenyte.game.world.World
import com.zenyte.game.world.entity.Entity
import com.zenyte.game.world.entity.Location
import com.zenyte.game.world.entity.SoundEffect
import com.zenyte.game.world.entity.masks.Animation
import com.zenyte.game.world.entity.masks.Graphics
import com.zenyte.game.world.entity.masks.Hit
import com.zenyte.game.world.entity.masks.HitType
import com.zenyte.game.world.entity.npc.combatdefs.AttackType
import com.zenyte.game.world.entity.player.Player
import com.zenyte.game.world.entity.player.action.combat.SpecialAttackScript
import com.zenyte.game.world.entity.player.action.combat.SpecialType
import com.zenyte.game.world.entity.player.container.RequestResult

/**
 * Represents a custom bow weapon with allegiance to a specific religion.
 *
 * @author Stan van der Bend
 */
sealed class GodBow(
    val itemId: Int,
    val soulCrystalItemId: Int,
    override val drawbackGfx: Graphics?,
    override val projectile: Projectile?,
) : ISpecialAttack, CrystalRecipe, AmmunitionDefinition {

    companion object { val all by lazy { listOf(Bandos, Saradomin, Armadyl, Zamorak) } }

    // special attack
    override val weapons: IntArray = intArrayOf(itemId)
    override val delay: Int = 5
    override val type: SpecialType = SpecialType.RANGED
    override val animation: Animation? = null
    override val attackType: AttackType = AttackType.RANGED
    override val graphics: Graphics? = null

    // ammunition
    override val itemIds: IntArray = intArrayOf(itemId)
    override val soundEffect: SoundEffect = SoundEffect(1352, 0, 0)
    override val bows: IntArray = intArrayOf(itemId)
    override val isRetrievable: Boolean = false
    override val isWeapon: Boolean = false

    // crystal recipe
    override val productItemId: Int = itemId
    override val crystalShardCost: Int = 0
    override val requiredCrafting: Int = 99
    override val requiredSmithing: Int = 99
    override val craftingExperience: Int = 1
    override val smithingExperience: Int = 1

    override fun isCompatible(bowId: Int): Boolean = bowId == itemId

    override fun hasMaterials(player: Player) =
        player.inventory.containsItem(soulCrystalItemId) && (
                player.inventory.containsItem(CrystalWeapon.BowOfFaerdhinen.inactiveId) ||
                        player.inventory.containsItem(CrystalWeapon.BowOfFaerdhinen.productItemId) ||
                        player.inventory.containsItem(CrystalCorrupted.BowOfFaerdhinen.productItemId))

    override fun deleteMaterials(player: Player): Boolean = player.inventory.let {
        it.deleteItem(soulCrystalItemId, 1).result == RequestResult.SUCCESS
                && (it.deleteItem(CrystalWeapon.BowOfFaerdhinen.inactiveId, 1).result == RequestResult.SUCCESS ||
                        it.deleteItem(CrystalWeapon.BowOfFaerdhinen.productItemId, 1).result == RequestResult.SUCCESS ||
                        it.deleteItem(CrystalCorrupted.BowOfFaerdhinen.productItemId, 1).result == RequestResult.SUCCESS)
    }

    override fun hasMaterialsForX(player: Player): Int {
        return if(hasMaterials(player))
            1
        else
            0
    }

    fun createCombat(target: Entity) = GodBowCombat(target, this)

    object Saradomin : GodBow(
        itemId = CustomItemId.SARADOMIN_BOW,
        soulCrystalItemId = CustomItemId.SARADOMIN_SOUL_CRYSTAL,
        drawbackGfx = Graphics(6009, 0, 96),
        projectile = Projectile(6008,  40, 36, 41, 21, 5, 11, 5)
    ) {

        override val specialAttackName: String = "Faithful shot"

        override val attack: SpecialAttackScript = SpecialAttackScript { player, combat, target ->
            val projectile = Projectile(1899, 10, 0, 50, 4, 4, 64, 5)
            val hitDelay = World.sendProjectile(player, target, projectile)
            player.animation = Animation(9168)
            WorldTasksManager.schedule({ player.graphics = null }, 2)
            player.sendSound(SpecialAttackScript.ARMADYL_EYE_SOUND)
            WorldTasksManager.scheduleOrExecute({
                val meleeHit = Hit(
                    player,
                    combat.getRandomHit(
                        player,
                        target,
                        combat.getMaxHit(player, 1.1, 1.0, false),
                        1.5,
                        AttackType.MAGIC
                    ),
                    HitType.MELEE
                )
                val magicHit = Hit(player, Utils.random(1, 16), meleeHit.isAccurate, HitType.MAGIC, 0)
                combat.delayHit(0, meleeHit, magicHit)
                World.sendSoundEffect(player, SpecialAttackScript.SARADOMINS_LIGHTNING_SWORD_SOUND)
                World.sendSoundEffect(player, SpecialAttackScript.SARADOMINS_LIGHTNING_SOUND)
                target.graphics = SpecialAttackScript.SARADOMINS_LIGHTNING_GFX
            }, hitDelay)
        }
    }

    object Bandos : GodBow(
        itemId = CustomItemId.BANDOS_BOW,
        soulCrystalItemId = CustomItemId.BANDOS_SOUL_CRYSTAL,
        drawbackGfx = Graphics(6007, 0, 96),
        projectile = Projectile(6006,  40, 36, 41, 21, 5, 11, 5)
    ) {
        override val specialAttackName: String = "Bow smash"

        override val attack: SpecialAttackScript = SpecialAttackScript { player, combat, target ->
            player.animation = Animation(6147)
            WorldTasksManager.schedule({
                val projectile = Projectile(1202, 10, 8, 33, 15, 10, 64, 5)
                val hitDelay = World.sendProjectile(player, target, projectile)
                player.graphics = Graphics(6000)
                player.sendSound(SpecialAttackScript.FAVOUR_OF_THE_WAR_GOD_SOUND)
                val hit = combat.getHit(player, target, 1.5, 1.0, 1.0, true)
                WorldTasksManager.scheduleOrExecute({
                    combat.delayHit(0, hit)
                    if (target is Player) {
                        target.prayerManager.apply {
                            drainPrayerPoints(hit.damage.coerceAtMost(prayerPoints))
                        }
                    }
                }, hitDelay)
            }, 1)
        }
    }

    object Zamorak : GodBow(
        itemId = CustomItemId.ZAMORAK_BOW,
        soulCrystalItemId = CustomItemId.ZAMORAK_SOUL_CRYSTAL,
        drawbackGfx = Graphics(6011, 0, 96),
        projectile = Projectile(6010,  40, 36, 41, 21, 5, 11, 5)
    ) {
        override val specialAttackName: String = "Freeze stab"

        private val snowBallProjectile = Projectile(1223, 33, 28, 37, 8, 10, 64, 5)
        private val barrageProjectile = Projectile(368, 0, 0, 37, 2, 8, 64, 5)

        override val attack: SpecialAttackScript = SpecialAttackScript { player, combat, target ->

            player.animation = Animation(12_000) // 2661, 2876, 3000
            player.graphics = Graphics(6003) // 2661, 2876, 3000
            player.sendSound(SpecialAttackScript.ICE_CLEAVE_SOUND)

            val hit = combat.getHit(player, target, 2.5, 1.0, 1.1, false)
            val hitDelay = minOf(
                World.sendProjectile(player, target, barrageProjectile),
                World.sendProjectile(player, target, snowBallProjectile)
            )

            combat.delayHit(hitDelay)

            WorldTasksManager.scheduleOrExecute({
                if (hit.damage > 0) {
                    target.resetWalkSteps()
                    target.freezeWithNotification(32)
                    target.graphics = SpecialAttackScript.ICE_CLEAVE_GFX
                }
            }, hitDelay)
        }
    }

    object Armadyl : GodBow(
        itemId = CustomItemId.ARMADYL_BOW,
        soulCrystalItemId = CustomItemId.ARMADYL_SOUL_CRYSTAL,
        drawbackGfx = Graphics(6005, 0, 96),
        projectile = Projectile(6004,  40, 36, 41, 21, 5, 11, 5)
    ) {

        override val specialAttackName: String = "Twister shot"

        override val attack: SpecialAttackScript = SpecialAttackScript { player, combat, target ->
            val projectile = Projectile(6001, 16, 10, 40, 15, 10, 64, 5)

            World.sendSoundEffect(player.position, KreeArra.TORNADO_SOUND)
            player.animation = Animation(9168)
            val hit = combat.getHit(player, target, 2.5, 1.35, 1.0, false)
            val hitDelay = World.sendProjectile(player, target, projectile)
            World.sendSoundEffect(
                Location(target.location),
                getHitSoundEffect(hit).withDelay(projectile.getProjectileDuration(player, target))
            )
            combat.delayHit(hitDelay, hit)
        }

        private fun getHitSoundEffect(hit: Hit) = (if (hit.damage == 0)
            KreeArra.TORNADO_SPLASH_SOUND
        else
            KreeArra.TORNADO_HIT_SOUND)
    }
}
